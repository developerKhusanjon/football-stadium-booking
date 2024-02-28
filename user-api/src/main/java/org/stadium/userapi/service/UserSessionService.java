package org.stadium.userapi.service;



import org.stadium.corelib.domain.UserCredential;
import org.stadium.userapi.controller.errors.BadRequestAlertException;
import org.stadium.userapi.controller.vm.LoginVM;
import org.stadium.userapi.service.enumaration.VerificationTypeSender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface UserSessionService {

    String sendSmsCode(UserCredential userCredential, LoginVM loginVM, VerificationTypeSender typeSender, String token, HttpServletRequest request, HttpServletResponse response);

    void save(UserCredential userCredential, String token, HttpServletRequest request);

    boolean verify(String token, String confirmationCode) throws BadRequestAlertException;

    String resendSmsCode(String token, String phone, String hash, HttpServletResponse response) throws BadRequestAlertException;



}
