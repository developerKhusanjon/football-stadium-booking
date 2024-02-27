package org.stadium.adminapi.service;

import org.stadium.adminapi.controller.vm.LoginVM;
import org.stadium.adminapi.service.dto.JWTTokenDto;

import javax.servlet.http.HttpServletRequest;


public interface AuthService {

    JWTTokenDto loginUser(HttpServletRequest request, LoginVM loginVM);

}
