package org.stadium.userapi.service;



import org.stadium.corelib.domain.User;
import org.stadium.userapi.controller.errors.BadRequestAlertException;
import org.stadium.userapi.service.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;


public interface UserService {
    UserDto save(UserDto userDto, User user) throws BadRequestAlertException;

    UserDto getOne(Long id) throws BadRequestAlertException;

    UserDto getMe(User user);

    Boolean sendSms(User user, String phone, String hash, HttpServletRequest request) throws BadRequestAlertException;

    Boolean changeNumber(User user, ChangeNumberDto otp, HttpServletRequest request) throws BadRequestAlertException;

    Void delete(User user);

    Boolean logout(User user, LogoutDto logoutDto) throws BadRequestAlertException;


    Result verifyDelete(User user, VerifyDeleteDto verifyDeleteDto) throws ParseException;

    String deleteUser(User user,String hash);


}
