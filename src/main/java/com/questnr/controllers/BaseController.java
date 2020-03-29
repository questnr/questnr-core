package com.questnr.controllers;

import com.questnr.model.entities.User;
import com.questnr.requests.LoginRequest;
import com.questnr.responses.LoginResponse;
import com.questnr.responses.SignUpResponse;
import com.questnr.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1")
public class BaseController {

    @Autowired
    BaseService baseService;

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    SignUpResponse signupUser(@Valid @RequestBody User user) {
        SignUpResponse response = baseService.signUp(user);

        return response;
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    LoginResponse loginUser(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = baseService.login(request);

        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
