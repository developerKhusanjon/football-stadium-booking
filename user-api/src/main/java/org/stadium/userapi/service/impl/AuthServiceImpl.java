package org.stadium.userapi.service.impl;

import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.stadium.corelib.domain.User;
import org.stadium.corelib.domain.UserCredential;
import org.stadium.corelib.domain.UserSession;
import org.stadium.corelib.repo.UserCredentialRepository;
import org.stadium.corelib.repo.UserRepository;
import org.stadium.corelib.repo.UserSessionRepository;
import org.stadium.userapi.controller.errors.BadRequestAlertException;
import org.stadium.userapi.controller.vm.LoginVM;
import org.stadium.userapi.security.jwt.JwtTokenProvider;
import org.stadium.userapi.service.AuthService;
import org.stadium.userapi.service.UserSessionService;
import org.stadium.userapi.service.dto.JWTTokenDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.stadium.userapi.service.dto.VerificationDto;
import org.stadium.userapi.service.enumaration.VerificationTypeSender;
import org.stadium.userapi.service.dto.AlertResponseDto;


@Service
public class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final UserSessionService userSessionService;

    private final UserSessionRepository userSessionRepository;

    public AuthServiceImpl(JwtTokenProvider tokenProvider,
                           UserRepository userRepository,
                           UserCredentialRepository userCredentialRepository,
                           UserSessionService userSessionService, UserSessionRepository userSessionRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.userCredentialRepository = userCredentialRepository;
        this.userSessionService = userSessionService;
        this.userSessionRepository = userSessionRepository;
    }

    @SneakyThrows
    @Override
    public JWTTokenDto loginUser(LoginVM loginVM, HttpServletRequest request, HttpServletResponse response) {
        Optional<User> user = userRepository.findByPhoneOrUsername(loginVM.getPhoneNumber(), loginVM.getPhoneNumber());
        if(user.isPresent() && !user.get().isDeleted()) {
            return login(user.get(), loginVM, request, response);
        } else return new JWTTokenDto(null, false, false);
    }

    @Override
    public JWTTokenDto register(LoginVM loginVM,
                                HttpServletRequest request, HttpServletResponse response) throws BadRequestAlertException {
        Optional<User> oldUser = userRepository.findByPhoneOrUsername(loginVM.getPhoneNumber(), loginVM.getPhoneNumber());
        if (oldUser.isPresent() && !oldUser.get().isDeleted())
            return new JWTTokenDto(null, false, false);

        User user = new User();
        user.setFirstname(loginVM.getName());
        user.setLastname(loginVM.getSurname());
        user.setPhone(loginVM.getPhoneNumber());
        user.setUsername(loginVM.getPhoneNumber());
        user.setLoginAt(LocalDateTime.now());
        user = userRepository.save(user);
        UserCredential userCredential = new UserCredential();
        userCredential.setPhone(loginVM.getPhoneNumber());
        userCredential.setUser(user);
        userCredentialRepository.save(userCredential);
        Map<String, String> info = addUserToContext(user, loginVM, request, response);
        return new JWTTokenDto(info.get("jwt"), true, false);
    }

    private Map<String, String> addUserToContext(User user, LoginVM loginVM,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) throws BadRequestAlertException {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        UserCredential userCredential = userCredentialRepository.findByPhoneAndUserId(user.getPhone(), user.getId())
                .orElseThrow(() -> new BadRequestAlertException("User not found", "userCredentials", "username", HttpStatus.NOT_FOUND));
        String otp = userSessionService.sendSmsCode(userCredential, loginVM, VerificationTypeSender.SMS, jwt, request, response);
        return Map.of("jwt",jwt,"otp",otp);
    }

    private JWTTokenDto login(User user, LoginVM loginVM,
                              HttpServletRequest request,
                              HttpServletResponse response) throws BadRequestAlertException {
        user.setEnabled(true);
        user = userRepository.save(user);
        Map<String, String> info = addUserToContext(user, loginVM, request, response);
        return new JWTTokenDto(info.get("jwt"), true, user.isCompleted());
    }

    @Override
    @Transactional
    public AlertResponseDto verifyUser(VerificationDto verification) throws BadRequestAlertException {
        boolean verify = userSessionService.verify(verification.getToken(), verification.getConfirmationCode());
        if (!verify) return new AlertResponseDto("Wrong verification code", false);
        Optional<UserSession> userSession = userSessionRepository.findByUserToken(verification.getToken());

        if(userSession.isPresent()) {
            userSession.get().setEnabled(true);
//            userSessionRepository.saveAndFlush(userSession.get());
            userSessionRepository.disableOtherSessions(
                    userSession.get().getUserCredential().getId(), userSession.get().getId());
            userSessionRepository.flush();
            userSessionRepository.update(userSession.get().getId());
        }
        return new AlertResponseDto("User successfully verified", true);
    }

    @Override
    public AlertResponseDto resendSms(String token, String phone, String hash, HttpServletResponse response) throws BadRequestAlertException {
        String smsCode = userSessionService.resendSmsCode(token, phone, hash, response);
        return new AlertResponseDto(smsCode, true);
    }

    @Override
    public JWTTokenDto refreshToken(String token, String phone, HttpServletRequest request) throws BadRequestAlertException {
        phone = phone.startsWith("+") ? phone : (phone.replace(" ", ""));
        User user = userRepository.findByPhoneAndToken(phone, token)
                .orElseThrow(() -> new BadRequestAlertException("User not found", "user", "phone",HttpStatus.NOT_FOUND));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        UserCredential userCredential = userCredentialRepository.findByPhoneAndUserId(user.getUsername(), user.getId())
                .orElseThrow(() -> new BadRequestAlertException("User not found", "userCredentials", "username",HttpStatus.NOT_FOUND));
        userSessionService.save(userCredential, jwt, request);
        return new JWTTokenDto(jwt, true, true);
    }
}
