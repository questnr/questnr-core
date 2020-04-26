package com.questnr.services.SocialLoginService;

import com.questnr.domain.SocialUserDetails;
import com.questnr.domain.facebook.FBAccessToken;
import com.questnr.domain.facebook.FBAccessTokenData;
import com.questnr.domain.facebook.FBData;
import com.questnr.domain.facebook.FBUserDetails;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.UserRepository;
import com.questnr.responses.LoginResponse;
import com.questnr.services.BaseService;
import com.questnr.services.EmailService;
import com.questnr.services.user.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class FBLoginService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${facebook.redirecturl}")
    private String REDIRECT_URI;

    @Value("${facebook.appid}")
    private String APP_ID;
    @Value("${facebook.appsecret}")
    private String APP_SECRET;

    @Autowired
    BaseService baseService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserService userManagementService;

    @Autowired
    private EmailService emailService;

    @Autowired
    SocialUserDetails socialUserDetails;

    private RestTemplate restTemplate = new RestTemplate();

    public LoginResponse facebookLogin(String fbAccessCode, String source) {

        if (fbAccessCode == null || fbAccessCode.isEmpty()) {
            return baseService.createErrorLoginResponse("Wrong Facebook credentials");
        }
        FBAccessToken fbToken;
        fbToken = getFBAccessTokenFromCode(fbAccessCode);
        LOGGER.info("Access token = {}", fbToken);

        if (fbToken == null) {
            return baseService.createErrorLoginResponse("Something went wrong with request");
        }

        String fbAppAccessToken;
        fbAppAccessToken = getFBAppAccessToken();
        FBAccessTokenData fbAccessTokenData = inspectFBAccessToken(fbToken.getAccess_token(), fbAppAccessToken);
        LOGGER.info("Verify token = {}", fbAccessTokenData);
        if (!fbAccessTokenData.isIs_valid() || fbAccessTokenData.getApp_id() != Long.valueOf(APP_ID)) {
            return baseService.createErrorLoginResponse("Wrong Facebook credentials");
        }

        LOGGER.debug("Token appears to be valid, fetching user details from token");
        FBUserDetails fbUserDetails;
        fbUserDetails = getUserDetailsFromAccessToken(fbToken.getAccess_token());


        LOGGER.info("In this case, use doesn't exist, lets create a new user");

        return saveLoginWithFacebook(fbUserDetails, source);

    }

    public LoginResponse saveLoginWithFacebook(FBUserDetails fbUserDetails, String source) {

        if (fbUserDetails != null) {
            User savedUser = userRepository.findByEmailId(fbUserDetails.getEmail());
            if (savedUser != null) {
                return baseService.createSuccessLoginResponse(savedUser);
            }
            savedUser = baseService.createUserFromSocialLogin(socialUserDetails.getUser(fbUserDetails), source);

            emailService.sendEmailOnSignUp(savedUser);
            return baseService.createSuccessLoginResponse(savedUser);

        } else {
            return baseService.createErrorLoginResponse("Wrong Facebook credentials");
        }
    }


    private FBAccessToken getFBAccessTokenFromCode(String code) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("client_id", APP_ID);
        urlParams.put("redirect_uri", REDIRECT_URI);
        urlParams.put("client_secret", APP_SECRET);
        urlParams.put("code", code);

        try {
            return restTemplate.getForObject(
                    "https://graph.facebook.com/oauth/access_token?client_id={client_id}&code={code}&client_secret"
                            + "={client_secret}&redirect_uri={redirect_uri}&scope=email",
                    FBAccessToken.class, urlParams);
        } catch (Exception e) {
            //TODO put a counter here for such failures and send report daily
            e.printStackTrace();
            return null;
        }
    }

    private boolean userIsAuthenticated(String access_token) {
        FBAccessTokenData fbAccessTokenData;
        try {
            fbAccessTokenData = inspectFBAccessToken(access_token, getFBAppAccessToken());
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }

        return !(!fbAccessTokenData.isIs_valid() || fbAccessTokenData.getApp_id() != Long.valueOf(APP_ID));
    }

    private FBAccessTokenData inspectFBAccessToken(String accessToken, String appAccessToken) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("input_token", accessToken);
        urlParams.put("access_token", appAccessToken);
        return restTemplate.getForObject(
                "https://graph.facebook.com/debug_token?input_token={input_token}&access_token={access_token}",
                FBData.class, urlParams).getData();
    }


    public FBUserDetails getUserDetailsFromAccessToken(String accessToken) {

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("access_token", accessToken);
        urlParams.put("fields", "id,name,email");
        LOGGER.info("Retrieving user details with {} and {}", accessToken, urlParams);
        return restTemplate.getForObject("https://graph.facebook.com/v3.0/me/?access_token={access_token}&fields={fields}",
                FBUserDetails.class, urlParams);
    }

    private String getFBAppAccessToken() {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("client_id", APP_ID);
        urlParams.put("client_secret", APP_SECRET);
        LOGGER.info("Retrieving app access token");
        String json = restTemplate.getForObject(
                "https://graph.facebook.com/oauth/access_token?client_id={client_id}&client_secret={client_secret"
                        + "}&grant_type=client_credentials",
                String.class, urlParams);
        try {
            return new JSONObject(json).getString("access_token");
        } catch (JSONException e) {
            return "";
        }
    }
}
