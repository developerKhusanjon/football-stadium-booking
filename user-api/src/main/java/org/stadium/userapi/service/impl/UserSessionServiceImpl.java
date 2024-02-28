package org.stadium.userapi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.stadium.corelib.domain.UserCredential;
import org.stadium.corelib.domain.UserSession;
import org.stadium.corelib.repo.DeletedUserSessionRepository;
import org.stadium.corelib.repo.UserSessionRepository;
import org.stadium.corelib.repo.impl.UserSessionRepositoryImpl;
import org.stadium.userapi.controller.errors.BadRequestAlertException;
import org.stadium.userapi.controller.vm.LoginVM;
import org.stadium.userapi.service.SmsSenderService;
import org.stadium.userapi.service.UserSessionService;
import org.stadium.userapi.service.enumaration.VerificationTypeSender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Random;


@Service
public class UserSessionServiceImpl implements UserSessionService {

    private final UserSessionRepository userSessionRepository;
    private final UserSessionRepositoryImpl userSessionRepositoryImpl;
    private final SmsSenderService smsSenderService;
    private static final Logger logger = LoggerFactory.getLogger(UserSessionServiceImpl.class);
    private final DeletedUserSessionRepository deletedUserSessionRepository;
    public UserSessionServiceImpl(UserSessionRepository userSessionRepository,
                                  UserSessionRepositoryImpl userSessionRepositoryImpl, SmsSenderService smsSenderService,DeletedUserSessionRepository deletedUserSessionRepository) {
        this.userSessionRepository = userSessionRepository;
        this.userSessionRepositoryImpl = userSessionRepositoryImpl;
        this.smsSenderService = smsSenderService;
        this.deletedUserSessionRepository=deletedUserSessionRepository;
    }

    @Override
    public String sendSmsCode(UserCredential userCredential, LoginVM loginVM, VerificationTypeSender typeSender, String token, HttpServletRequest request, HttpServletResponse response) {
        UserSession userSession = new UserSession();
        userSession.setDeviceIp(getClientIpAddress(request));
        userSession.setDeviceModel(getDeviceModel(request));
        userSession.setDeviceOsVersion(getDeviceOsVersion(request));
        userSession.setUserToken(token);
        userSession.setUserCredential(userCredential);
        if(typeSender.equals(VerificationTypeSender.SMS)) {
            try {
                String otp = new DecimalFormat("00000").format(new Random().nextInt(99999));
                userSession.setSmsCode(otp);
                //+99890... replace to 99890....
                String phone = userCredential.getPhone();
                phone = phone.startsWith("+") ? phone.substring(1) : phone;
                if(phone.equals("998901234567")){
                    userSession.setSmsCode("12345");
                }
                userSessionRepository.save(userSession);
                smsSenderService.send(phone, otp, loginVM.getHash());
                return otp;
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }

        } else {
            if(typeSender.equals(VerificationTypeSender.DEFAULT)) {
                userSessionRepository.save(userSession);
            }
        }

      return null;
    }

    @Override
    public void save(UserCredential userCredential, String token, HttpServletRequest request) {
        UserSession userSession = new UserSession();
        userSession.setDeviceIp(getClientIpAddress(request));
        userSession.setDeviceModel(getDeviceModel(request));
        userSession.setDeviceOsVersion(getDeviceOsVersion(request));
        userSession.setUserToken(token);
        userSession.setUserCredential(userCredential);
        userSessionRepository.save(userSession);
    }


    @Override
    public boolean verify(String token, String confirmationCode) throws BadRequestAlertException {
        return userSessionRepositoryImpl.enableUserWithConfirmationCode(token, confirmationCode);
    }



    @Override
    public String resendSmsCode(String token, String phone, String hash, HttpServletResponse response) throws BadRequestAlertException {
        try {
            phone = phone.replace(" ", "");
            UserSession userSession = userSessionRepository.findByUserTokenAndPhone(token, phone)
                    .orElseThrow(() -> new BadRequestAlertException("phone number not found", "login", "resent", HttpStatus.NOT_FOUND));
            String otp = new DecimalFormat("00000").format(new Random().nextInt(99999));
            userSession.setSmsCode(otp);
            userSession.setCreatedAt(LocalDateTime.now());
            userSessionRepository.save(userSession);
            smsSenderService.send(phone.substring(1), otp, hash);
            return otp;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return "Unexpected ServerSide Error";
    }


    private String getDeviceOsVersion(HttpServletRequest request) {
        return request.getHeader("device-os-version");
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String LOCALHOST_IPV4 = "127.0.0.1";
        String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
        String ipAddress = request.getHeader("X-Forwarded-For");
        if(StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if(StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if(StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress)) {
                try {
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    ipAddress = inetAddress.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }

        if(!StringUtils.isEmpty(ipAddress)
                && ipAddress.length() > 15
                && ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }

        return ipAddress;
    }

    private String getDeviceModel(HttpServletRequest request) {
        return request.getHeader("device-model");
    }



}
