package org.stadium.userapi.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stadium.userapi.controller.errors.BadRequestAlertException;
import org.stadium.userapi.controller.vm.LoginVM;
import org.stadium.userapi.security.jwt.JwtAuthenticationFilter;
import org.stadium.userapi.service.AuthService;
import org.stadium.userapi.service.dto.AlertResponseDto;
import org.stadium.userapi.service.dto.JWTTokenDto;
import org.stadium.userapi.service.dto.RefreshDto;
import org.stadium.userapi.service.dto.VerificationDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @Operation(description = "Login user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "if user is entered to joyla successfully, return JWT",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JWTTokenDto.class))}),
            @ApiResponse(responseCode = "409", description = "required fields",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class),
                            examples = {@ExampleObject(value = "{username:'username is required'...}")})),
            @ApiResponse(responseCode = "400", description = "Other situations throw  BadRequestAlert exception",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestAlertException.class)))})
    @PostMapping("/login")
    public ResponseEntity<JWTTokenDto> login(@RequestBody @Valid LoginVM loginVM, HttpServletRequest request, HttpServletResponse response) {
        JWTTokenDto jwt = authService.loginUser(loginVM, request, response);
        if(jwt.isSuccess()) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + jwt.getToken());
//            httpHeaders.add("smsCode", jwt.getSmsCode());
            return new ResponseEntity<>(jwt, httpHeaders, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body(jwt);
    }

    @Operation(description = "Sign up user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "if user is entered to joyla successfully, return JWT",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JWTTokenDto.class))}),
            @ApiResponse(responseCode = "409", description = "required fields",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class),
                            examples = {@ExampleObject(value = "{username:'username is required'...}")})),
            @ApiResponse(responseCode = "400", description = "Other situations throw  BadRequestAlert exception",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestAlertException.class)))})
    @PostMapping("/sign-up")
    public ResponseEntity<JWTTokenDto> signUp(@RequestBody @Valid LoginVM loginVM, HttpServletRequest request, HttpServletResponse response) throws BadRequestAlertException {
        JWTTokenDto jwt = authService.register(loginVM, request, response);
        if(jwt.isSuccess()) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + jwt.getToken());
//            httpHeaders.add("smsCode", jwt.getSmsCode());
            return new ResponseEntity<>(jwt, httpHeaders, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body(jwt);
    }



    @PostMapping("/verify")
    private ResponseEntity<AlertResponseDto> verifyUser(@RequestBody VerificationDto verification) throws BadRequestAlertException {
        AlertResponseDto alertResponse = authService.verifyUser(verification);
        if (alertResponse.isSuccess()) return ResponseEntity.ok(alertResponse);
        else return ResponseEntity.badRequest().body(alertResponse);
    }

    @PostMapping("/resend")
    public ResponseEntity<AlertResponseDto> resend(@RequestBody RefreshDto refreshDto, HttpServletResponse response) throws BadRequestAlertException {
        return ResponseEntity.ok(authService.resendSms(refreshDto.getToken(), refreshDto.getPhone(), refreshDto.getHash(), response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JWTTokenDto> refresh(@RequestBody RefreshDto refreshDto, HttpServletRequest request) throws BadRequestAlertException {
        return ResponseEntity.ok(authService.refreshToken(refreshDto.getToken(), refreshDto.getPhone(), request));
    }

}
