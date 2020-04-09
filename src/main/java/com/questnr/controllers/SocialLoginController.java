package com.questnr.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;

import com.questnr.domain.facebook.FBUserDetails;
import com.questnr.domain.google.GoogleUserDetails;
import com.questnr.responses.LoginResponse;
import com.questnr.services.SocialLoginService.FBLoginService;
import com.questnr.services.SocialLoginService.GoogleLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SocialLoginController {

  @Autowired
  private FBLoginService fbLoginService;

  @Autowired
  private GoogleLoginService googleLoginService;

  private static final Logger LOGGER = LoggerFactory.getLogger(SocialLoginController.class);

  @Value("${facebook.redirecturl}")
  private String FB_REDIRECT_URI;

  @Value("${facebook.appid}")
  private String FB_APP_ID;
  @Value("${facebook.appsecret}")
  private String FB_APP_SECRET;

  @Value("${google.redirecturl}")
  private String GOOGLE_REDIRECT_URI;

  @Value("${google.clientid}")
  private String GOOGLE_CLIENT_ID;

  @Value("${google.clientsecret}")
  private String GOOGLE_CLIENT_SECRET;

  public SocialLoginController() {
  }

  @GetMapping("/facebook/get-login-uri")
  public String geFacebooktLoginUri() {
    String uri = "https://www.facebook.com/v3.0/dialog/oauth?client_id=" + FB_APP_ID
        + "&scope=email&redirect_uri=" + FB_REDIRECT_URI;
        //+ "&state=" + genCSRF();
    return uri;
  }

  @GetMapping("/facebook/login")
  public ResponseEntity<?> facebookLogin(@RequestParam("code") String code,
      @RequestParam("state") String state,String source,
      HttpServletResponse httpServletResponse) throws IOException {
    LOGGER.info("Entering login service");
    ResponseEntity<LoginResponse> response = null;

    //TODO validate csrf too
    List<String> messages = new ArrayList<String>();
    LoginResponse loginResponse = fbLoginService.facebookLogin(code,source);
    response = new ResponseEntity<>(loginResponse, HttpStatus.OK);
    return response;
  }

  @GetMapping("/facebook/login/mobile")
  public ResponseEntity<?> facebookLoginWithAccessToken(@RequestParam("token") String token,
      HttpServletResponse httpServletResponse) throws IOException {
    LOGGER.info("Entering login service");
    ResponseEntity<LoginResponse> response = null;

    FBUserDetails userDetailsFromAccessToken = fbLoginService.getUserDetailsFromAccessToken(token);

    LoginResponse loginResponse = fbLoginService.saveLoginWithFacebook(userDetailsFromAccessToken, new LoginResponse(),null);
    response = new ResponseEntity<>(loginResponse, HttpStatus.OK);
    return response;
  }

  @GetMapping("/google/get-login-uri")
  public String geGoogletLoginUri() {
    String uri = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + GOOGLE_CLIENT_ID
        + "&scope=profile email&redirect_uri=" + GOOGLE_REDIRECT_URI
        + "&response_type=code";
        //+ "&state=" + genCSRF();
    return uri;
  }

  @GetMapping("/google/login")
  public ResponseEntity<?> googleLogin(@RequestParam("code") String code,
      @RequestParam("state") String state,@RequestParam("source")String source,
      HttpServletResponse httpServletResponse) throws IOException {
    LOGGER.info("Entering login service");
    ResponseEntity<LoginResponse> response = null;

    //TODO validate csrf too
    List<String> messages = new ArrayList<String>();
    LoginResponse loginResponse = googleLoginService.googleLogin(code,source);
    response = new ResponseEntity<>(loginResponse, HttpStatus.OK);
    return response;
  }


  @PostMapping("/google/login/mobile")
  public ResponseEntity<?> googleLoginWithAccessToken(@RequestBody GoogleUserDetails googleUserDetails,
      HttpServletResponse httpServletResponse) throws IOException {
    LOGGER.info("Entering login service");
    ResponseEntity<LoginResponse> response = null;

    LoginResponse loginResponse = googleLoginService.saveLoginWithGoogle(googleUserDetails,new LoginResponse(),googleUserDetails.getSignUpSource());
    response = new ResponseEntity<>(loginResponse, HttpStatus.OK);
    return response;
  }


  private String genCSRF() {
    return UUID.randomUUID().toString();
  }
}
