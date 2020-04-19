package com.questnr.controllers;

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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/oauth2")
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
    public String getFacebookLoginUri() {
        String uri = "https://www.facebook.com/v3.0/dialog/oauth?client_id=" + FB_APP_ID
                + "&scope=email&redirect_uri=" + FB_REDIRECT_URI;
        //+ "&state=" + genCSRF();
        return uri;
    }

    @GetMapping("/facebook/login")
    public ResponseEntity<?> facebookLogin(@RequestParam("code") String code,
                                           @RequestParam("state") String state, String source,
                                           HttpServletResponse httpServletResponse) {
        LOGGER.info("Entering login service");

        // @TODO validate csrf too
        LoginResponse loginResponse = fbLoginService.facebookLogin(code, source);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @GetMapping("/facebook/login/mobile")
    public ResponseEntity<?> facebookLoginWithAccessToken(@RequestParam("token") String token,
                                                          HttpServletResponse httpServletResponse) {
        LOGGER.info("Entering login service");

        FBUserDetails userDetailsFromAccessToken = fbLoginService.getUserDetailsFromAccessToken(token);

        LoginResponse loginResponse = fbLoginService.saveLoginWithFacebook(userDetailsFromAccessToken, null);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @GetMapping("/google/get-login-uri")
    public String getGoogleLoginUri() {
        String uri = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + GOOGLE_CLIENT_ID
                + "&scope=profile email&redirect_uri=" + GOOGLE_REDIRECT_URI
                + "&response_type=code";
        return uri;
    }

    @GetMapping("/google/login")
    public ResponseEntity<?> googleLogin(@RequestParam("code") String code,
                                         @RequestParam("state") String state, @RequestParam("source") String source,
                                         HttpServletResponse httpServletResponse) {
        LOGGER.info("Entering login service");

        // @TODO validate csrf too
        LoginResponse loginResponse = googleLoginService.googleLogin(code, source);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }


    @GetMapping("/google/login/token")
    public ResponseEntity<?> googleLoginWithIdToken(@RequestParam("idToken") String idToken,  @RequestParam("source") String source) {
        LOGGER.info("Entering login service");

        // @TODO validate csrf too
        LoginResponse loginResponse = googleLoginService.googleLoginWithIdToken(idToken, source);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }


    @PostMapping("/google/login/mobile")
    public ResponseEntity<?> googleLoginWithAccessToken(@RequestBody GoogleUserDetails googleUserDetails,
                                                        HttpServletResponse httpServletResponse) {
        LOGGER.info("Entering login service");

        LoginResponse loginResponse = googleLoginService.saveLoginWithGoogle(googleUserDetails, googleUserDetails.getSignUpSource());
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);

    }


    private String genCSRF() {
        return UUID.randomUUID().toString();
    }
}
