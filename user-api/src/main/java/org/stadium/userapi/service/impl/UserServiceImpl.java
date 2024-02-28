package org.stadium.userapi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.stadium.corelib.domain.*;
import org.stadium.corelib.repo.*;
import org.stadium.userapi.controller.errors.BadRequestAlertException;
import org.stadium.userapi.mapper.UserMapper;
import org.stadium.userapi.security.CurrentUser;
import org.stadium.userapi.service.SmsSenderService;
import org.stadium.userapi.service.UserService;
import org.stadium.userapi.service.dto.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;


@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserCredentialRepository userCredentialRepository;
    private final UserSessionRepository userSessionRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final OtpHolderRepository otpHolderRepository;

    private final DeletedUserRepository deletedUserRepository;
    private final DeletedUserSessionRepository deletedUserSessionRepository;
    private final SmsSenderService smsSenderService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, UserCredentialRepository userCredentialRepository,
                           UserSessionRepository userSessionRepository, DeviceTokenRepository deviceTokenRepository,
                           OtpHolderRepository otpHolderRepository, DeletedUserRepository deletedUserRepository, DeletedUserSessionRepository deletedUserSessionRepository, SmsSenderService smsSenderService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userCredentialRepository = userCredentialRepository;
        this.userSessionRepository = userSessionRepository;
        this.deviceTokenRepository = deviceTokenRepository;
        this.otpHolderRepository = otpHolderRepository;
        this.deletedUserRepository = deletedUserRepository;
        this.deletedUserSessionRepository = deletedUserSessionRepository;
        this.smsSenderService = smsSenderService;
    }


    @Override
    public UserDto save(UserDto userDto, User user) throws BadRequestAlertException {

        if (Objects.nonNull(userDto.getFirstname()))
            user.setFirstname(userDto.getFirstname());

        if (Objects.nonNull(userDto.getLastname()))
            user.setLastname(userDto.getLastname());

        if (Objects.nonNull(userDto.getMiddleName()))
            user.setMiddleName(userDto.getMiddleName());

        if (!user.isCompleted() &&
                (Objects.isNull(userDto.getFirstname()) || Objects.equals(userDto.getFirstname(), ""))) {
            user.setFirstname("DemoUser" + new DecimalFormat("00000").format(new Random().nextInt(99999)));
        }

        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto getOne(Long id) throws BadRequestAlertException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestAlertException("User not found", "user", "id", HttpStatus.NOT_FOUND));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto getMe(User user) {
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public Boolean sendSms(User user, String phone, String hash, HttpServletRequest request) throws BadRequestAlertException {
        phone = phone.startsWith("+") ? phone.substring(1).replace(" ", "") : phone.replace(" ", "");
        boolean existsByPhone = userRepository.existsByPhone(phone);
        if (!existsByPhone) {
            String otp = new DecimalFormat("00000").format(new Random().nextInt(99999));
            smsSenderService.send(phone, otp, hash);
            otpHolderRepository.deleteAllByPhone(phone);
            otpHolderRepository.save(OtpHolder.builder()
                    .phone(phone).otp(otp).build());
            return Boolean.TRUE;
        }
        throw new BadRequestAlertException("this phone number already exits", "user", "phone");
    }

    @Override
    @Transactional
    public Boolean changeNumber(User user, ChangeNumberDto otp, HttpServletRequest request) throws BadRequestAlertException {
        try {
            if (otpHolderRepository.existsByPhoneAndOtp(otp.getPhone(), otp.getOtp())) {
                userCredentialRepository.updatePhone(otp.getPhone(), user.getId(), user.getPhone());
                user.setPhone(otp.getPhone());
                user.setUsername(otp.getPhone());
                userRepository.save(user);
                otpHolderRepository.deleteAllByPhone(otp.getPhone());
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            throw new BadRequestAlertException(e.getMessage(), "user", "changePhone");
        }
        return Boolean.FALSE;
    }

    @Override
    public Void delete(User user) {
        user.setDeleted(true);
        userRepository.save(user);
        return null;
    }

    @Override
    @Transactional
    public Boolean logout(User user, LogoutDto logoutDto) throws BadRequestAlertException {
        Optional<DeviceToken> deviceToken = deviceTokenRepository.findByUserIdAndPushToken(user.getId(), logoutDto.getDeviceToken());
        if (deviceToken.isEmpty())
            throw new BadRequestAlertException("PushToken not found", "Firebase", "PushToken", HttpStatus.NOT_FOUND);
        try {
            deviceTokenRepository.deleteById(deviceToken.get().getId());
            userSessionRepository.deleteUserSession(deviceToken.get().getUserSession().getId());
            return Boolean.TRUE;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Boolean.FALSE;
        }
    }


    @Override
    public String deleteUser(User user,String hash){
        try{
            DeletedUserSession deletedUserSession=new DeletedUserSession();
            String phoneNumber = user.getPhone();
            phoneNumber = phoneNumber.startsWith("+") ? phoneNumber.substring(1) : (phoneNumber);
            deletedUserSession.setUser_id(user.getId());
            String otp = new DecimalFormat("00000").format(new Random().nextInt(99999));
            deletedUserSession.setCreatedAt(LocalDateTime.now());
            deletedUserSession.setSmsCode(otp);
            deletedUserSessionRepository.save(deletedUserSession);
            smsSenderService.send(phoneNumber, otp, hash);
            return "Sms code sent to "+phoneNumber;
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return "Unexpected ServerSide error!";
    }
    @Override
    public Result verifyDelete(@CurrentUser User user, VerifyDeleteDto verifyDeleteDto) throws ParseException {
        Long userId = user.getId();
        Optional<DeletedUserSession> deletedUserSession = deletedUserSessionRepository.findByUserId(userId);
        LocalDateTime now=LocalDateTime.now();
        LocalDateTime smsSentTime=deletedUserSession.get().getCreatedAt();
        long diff=smsSentTime.until(now, ChronoUnit.MINUTES);

        if (deletedUserSession.isPresent()) {
            if(deletedUserSession.get().getSmsCode().equals(verifyDeleteDto.getConfirmationCode())&&diff<=15) {
                DeletedUsers deletedUsers = new DeletedUsers();
                deletedUsers.setFirstName(user.getFirstname());
                deletedUsers.setComment(verifyDeleteDto.getComment());
                deletedUsers.setLastName(user.getLastname());
                deletedUsers.setPhoneNumber(user.getPhone());
                deletedUsers.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
                DeletedUsers deleteUser = deletedUserRepository.save(deletedUsers);

                userRepository.deleteUserById(user.getId());
                return new Result("Successfully deleted", true);
            }
            else return new Result("Verification code is wrong or expired!",false);
        }
        else return new Result("User not found!", false);
    }

}
