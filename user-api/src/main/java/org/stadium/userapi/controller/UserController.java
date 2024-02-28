package org.stadium.userapi.controller;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stadium.corelib.domain.User;
import org.stadium.userapi.controller.errors.BadRequestAlertException;
import org.stadium.userapi.controller.vm.LoginVM;
import org.stadium.userapi.security.CurrentUser;
import org.stadium.userapi.service.UserService;
import org.stadium.userapi.service.dto.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PutMapping
    public ResponseEntity<UserDto> updateUserDetails(@RequestBody UserDto userDto, @Parameter(hidden = true) @CurrentUser User user) throws BadRequestAlertException {
        userDto = userService.save(userDto, user);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getOneUser(@PathVariable Long id) throws BadRequestAlertException {
        return ResponseEntity.ok(userService.getOne(id));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe(@CurrentUser @Parameter(hidden = true) User user) {
        return ResponseEntity.ok(userService.getMe(user));
    }

    @PutMapping("/sms")
    public ResponseEntity<Boolean> getSmsToChangeNumber(@CurrentUser @Parameter(hidden = true) User user, @Valid @RequestBody LoginVM loginVM, HttpServletRequest request) throws BadRequestAlertException {
        return ResponseEntity.ok(userService.sendSms(user, loginVM.getPhoneNumber(), loginVM.getHash(), request));
    }

    @PutMapping("/phone")
    public ResponseEntity<Boolean> changeNumber(@CurrentUser @Parameter(hidden = true) User user, @Valid @RequestBody ChangeNumberDto numberDto, HttpServletRequest request) throws BadRequestAlertException {
        return ResponseEntity.ok(userService.changeNumber(user, numberDto, request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(@CurrentUser User user, @RequestBody LogoutDto logoutDto) throws BadRequestAlertException {
        return ResponseEntity.ok(userService.logout(user, logoutDto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@CurrentUser User user){
        return ResponseEntity.ok(userService.delete(user));
    }

    @DeleteMapping("/delete-verify")
    public HttpEntity<?> deleteVerify(@CurrentUser User user, @RequestBody VerifyDeleteDto verifyDeleteDto) throws ParseException {
        Result result=userService.verifyDelete(user,verifyDeleteDto);
        return ResponseEntity.status(result.isSuccess()?200:404).body(result);
    }

    @DeleteMapping("/delete")
    public String delete(@CurrentUser User user,String hash){
        return userService.deleteUser(user,hash);
    }

   }
