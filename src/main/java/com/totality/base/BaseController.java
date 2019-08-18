package com.totality.base;

import com.totality.model.entities.User;
import com.totality.requests.LoginRequest;
import com.totality.requests.UsersRequest;
import com.totality.responses.LoginResponse;
import com.totality.responses.SignUpResponse;
import com.totality.services.BaseService;
import com.totality.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class BaseController {



  @Autowired
  BaseService service;

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
