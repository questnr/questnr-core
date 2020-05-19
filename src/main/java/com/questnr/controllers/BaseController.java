package com.questnr.controllers;

import com.questnr.requests.LoginRequest;
import com.questnr.requests.UserEmailRequest;
import com.questnr.requests.UserRequest;
import com.questnr.requests.UsernameRequest;
import com.questnr.responses.LoginResponse;
import com.questnr.responses.ResetPasswordResponse;
import com.questnr.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1")
public class BaseController {

    @Autowired
    BaseService baseService;

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    LoginResponse signUpUser(@Valid @RequestBody UserRequest userRequest) {
        return baseService.signUp(userRequest);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    LoginResponse loginUser(@Valid @RequestBody LoginRequest request) {
        return baseService.login(request);
    }

    @RequestMapping(value = "/check-username", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void checkUsername(@RequestBody UsernameRequest usernameRequest) {
        baseService.checkIfUsernameIsTakenWithException(usernameRequest.getUsername());
    }

    @RequestMapping(value = "/check-email", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void checkEmail(@RequestBody UserEmailRequest userEmailRequest) {
        baseService.checkIfEmailIsTakenWithException(userEmailRequest.getEmail());
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    @ResponseBody
    ResetPasswordResponse createPasswordResetRequest(@RequestBody UserEmailRequest userEmailRequest) {

        // ResponseEntity<ResetPasswordResponse> res = null;
        ResetPasswordResponse response = baseService.generatePasswordResetToken(userEmailRequest.getEmail());
        return response;
    }
}
