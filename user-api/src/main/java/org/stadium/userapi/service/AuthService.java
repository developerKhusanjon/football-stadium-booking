package org.stadium.userapi.service;


import org.stadium.userapi.controller.errors.BadRequestAlertException;
import org.stadium.userapi.controller.vm.LoginVM;
import org.stadium.userapi.service.dto.AlertResponseDto;
import org.stadium.userapi.service.dto.JWTTokenDto;
import org.stadium.userapi.service.dto.VerificationDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface AuthService {
    JWTTokenDto loginUser(LoginVM loginVM, HttpServletRequest request, HttpServletResponse response);

    JWTTokenDto register(LoginVM loginVM, HttpServletRequest request, HttpServletResponse response) throws BadRequestAlertException;

    AlertResponseDto verifyUser(VerificationDto verification) throws BadRequestAlertException;

    AlertResponseDto resendSms(String token, String phone, String hash, HttpServletResponse response) throws BadRequestAlertException;

    JWTTokenDto refreshToken(String token, String phone, HttpServletRequest request) throws BadRequestAlertException;

}
