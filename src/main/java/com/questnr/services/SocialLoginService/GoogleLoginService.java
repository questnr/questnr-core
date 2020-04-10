package com.questnr.services.SocialLoginService;

import com.questnr.domain.SocialUserDetails;
import com.questnr.domain.google.GoogleToken;
import com.questnr.domain.google.GoogleUserDetails;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.UserRepository;
import com.questnr.responses.LoginResponse;
import com.questnr.services.BaseService;
import com.questnr.services.EmailService;
import com.questnr.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleLoginService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${google.redirecturl}")
    private String REDIRECT_URI;

    @Value("${google.clientid}")
    private String CLIENT_ID;
    @Value("${google.clientsecret}")
    private String CLIENT_SECRET;

    @Autowired
    BaseService baseService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    UserService userService;

    private RestTemplate restTemplate = new RestTemplate();

    public LoginResponse googleLogin(String googleAccessCode, String source) {

        if (googleAccessCode == null || googleAccessCode.isEmpty()) {
            return baseService.createErrorLoginResponse("Wrong Google credentials");
        }

        GoogleToken googleToken;
        googleToken = getGoogleAccessTokenFromCode(googleAccessCode);
        if (googleToken == null) {
            return baseService.createErrorLoginResponse("Something appears to have gone wrong with request");
        }


        LOGGER.info("Token Object = {}", googleToken);

        if (googleToken.getExpires_in() <= 0) {
            return baseService.createErrorLoginResponse("Token appears to have expired [Google]");
        }

        LOGGER.debug("Token appears to be valid, fetching user details from token");
        GoogleUserDetails googleUserDetails;
        googleUserDetails = getUserDetailsFromAIDToken(googleToken.getId_token());

        return saveLoginWithGoogle(googleUserDetails, source);
    }

    public LoginResponse saveLoginWithGoogle(GoogleUserDetails googleUserDetails, String source) {

        if (googleUserDetails != null) {
            User savedUser = userRepository.findByEmailId(googleUserDetails.getEmail());
            if (savedUser != null) {
                return baseService.createSuccessLoginResponse(savedUser);
            }

            LOGGER.info("In this case, use doesn't exist, lets create a new user");

            // TODO save details for Google ID, other info to fetch from Google
            savedUser = baseService.createUserFromSocialLogin(SocialUserDetails.getUser(googleUserDetails), source);

            emailService.sendEmailOnSignUp(savedUser);
            return baseService.createSuccessLoginResponse(savedUser);
        } else {
            return baseService.createErrorLoginResponse("Wrong Google credentials");
        }
    }


    private GoogleToken getGoogleAccessTokenFromCode(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("client_id", CLIENT_ID);
        map.add("redirect_uri", REDIRECT_URI);
        map.add("client_secret", CLIENT_SECRET);
        map.add("code", code);
        map.add("grant_type", "authorization_code");
        map.add("scope", "email");

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<MultiValueMap<String, String>>(map, headers);
        String url = "https://www.googleapis.com/oauth2/v4/token";

        try {
            ResponseEntity<GoogleToken> response2 =
                    restTemplate.postForEntity(url, request, GoogleToken.class);

            LOGGER.info(response2.toString());

            return response2.getBody();
        } catch (Exception e) {
            //TODO collect report for such failures and send daily
            e.printStackTrace();
            return null;
        }
    }

    public GoogleUserDetails getUserDetailsFromAIDToken(String idToken) {

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("id_token", idToken);
        GoogleUserDetails data = restTemplate.getForObject("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token={id_token}",
                GoogleUserDetails.class, urlParams);
        return data;
    }
}
