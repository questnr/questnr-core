package com.questnr.controllers;

import com.questnr.model.entities.User;
import com.questnr.requests.LoginRequest;
import com.questnr.requests.UserEmailRequest;
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
    LoginResponse signUpUser(@Valid @RequestBody User user) {
        return baseService.signUp(user);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    LoginResponse loginUser(@Valid @RequestBody LoginRequest request) {
        return baseService.login(request);
    }

    @RequestMapping(value = "/check-username", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void checkUsername(@RequestParam String username) {
        baseService.checkIfUsernameIsTakenWithException(username);
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    @ResponseBody
    ResetPasswordResponse createPasswordResetRequest(@RequestBody UserEmailRequest userEmailRequest) {

        // ResponseEntity<ResetPasswordResponse> res = null;
        ResetPasswordResponse response = baseService.generatePasswordResetToken(userEmailRequest.getUserEmail());
        return response;
    }
}
