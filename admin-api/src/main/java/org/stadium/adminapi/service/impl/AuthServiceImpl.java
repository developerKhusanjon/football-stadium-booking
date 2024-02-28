package org.stadium.adminapi.service.impl;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.stadium.adminapi.controller.vm.LoginVM;
import org.stadium.adminapi.security.jwt.JwtTokenProvider;
import org.stadium.adminapi.service.AdminSessionService;
import org.stadium.adminapi.service.AuthService;
import org.stadium.adminapi.service.dto.JWTTokenDto;
import org.stadium.corelib.domain.Admin;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;


@Service
public class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider tokenProvider;
    private final AdminSessionService adminSessionService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthServiceImpl(JwtTokenProvider tokenProvider, AdminSessionService adminSessionService,
                           AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.adminSessionService = adminSessionService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }


    @Override
    public JWTTokenDto loginUser(HttpServletRequest request, @NotNull LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
        Authentication authentication = null;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);
            adminSessionService.save(request, (Admin) authentication.getPrincipal(), jwt);
            return new JWTTokenDto(jwt, true);
        } catch (Exception e) {
            return (new JWTTokenDto("Username or Password invalid", false));
        }
    }
}
