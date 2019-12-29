package com.questnr.base;

import com.questnr.model.entities.User;
import com.questnr.requests.LoginRequest;
import com.questnr.responses.LoginResponse;
import com.questnr.responses.SignUpResponse;
import com.questnr.services.PostActionService;
import com.questnr.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class BaseController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/signup", method = RequestMethod.PUT)
    SignUpResponse signup(@RequestBody User user) {
        SignUpResponse response = userService.signUp(user);

        return response;
    }


    @RequestMapping(value = "/login", method = RequestMethod.PUT)
    LoginResponse loginUser(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);

        return response;
    }

}
