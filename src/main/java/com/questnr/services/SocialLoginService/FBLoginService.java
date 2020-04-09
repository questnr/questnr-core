package com.questnr.services.SocialLoginService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.questnr.common.enums.AuthorityName;
import com.questnr.common.enums.LoginType;
import com.questnr.common.enums.SignUpSourceType;
import com.questnr.domain.facebook.FBAccessToken;
import com.questnr.domain.facebook.FBAccessTokenData;
import com.questnr.domain.facebook.FBData;
import com.questnr.domain.facebook.FBUserDetails;
import com.questnr.model.entities.Authority;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.AuthorityRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.responses.LoginResponse;
import com.questnr.security.JwtTokenUtil;
import com.questnr.security.JwtUser;
import com.questnr.services.EmailService;
import com.questnr.services.user.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
  private UserRepository userRepository;

  @Autowired
  private AuthorityRepository authRepository;

  @Autowired
  private JwtTokenUtil jwtUtil;

  @Autowired
  private UserDetailsService userDetailsService;
  
  @Autowired
  UserService userManagementService;

  @Autowired
  private EmailService emailService;

  private RestTemplate restTemplate = new RestTemplate();

  public LoginResponse facebookLogin(String fbAccessCode, String source) {

    LoginResponse response = new LoginResponse();
    if (fbAccessCode == null || fbAccessCode.isEmpty()) {
      response.setLoginSucces(false);
      response.setErrorMessage("Wrong Facebook credentials");
      return response;
    }
    FBAccessToken fbToken;
    fbToken = getFBAccessTokenFromCode(fbAccessCode);
    LOGGER.info("Access token = {}", fbToken);

    if (fbToken == null)
    {
      response.setLoginSucces(false);
      response.setErrorMessage("Something went wrong with request");
      return response;
    }

    String fbAppAccessToken;
    fbAppAccessToken = getFBAppAccessToken();
    FBAccessTokenData fbAccessTokenData = inspectFBAccessToken(fbToken.getAccess_token(), fbAppAccessToken);
    LOGGER.info("Verify token = {}", fbAccessTokenData);
    if (!fbAccessTokenData.isIs_valid() || fbAccessTokenData.getApp_id() != Long.valueOf(APP_ID)) {
      response.setLoginSucces(false);
      response.setErrorMessage("Wrong Facebook credentials");
      return response;
    }

    LOGGER.debug("Token appears to be valid, fetching user details from token");
    FBUserDetails fbUserDetails;
    fbUserDetails = getUserDetailsFromAccessToken(fbToken.getAccess_token());


    LOGGER.info("In this case, use doesn't exist, lets create a new user");

    return saveLoginWithFacebook(fbUserDetails,response,source);

  }

  public LoginResponse saveLoginWithFacebook(FBUserDetails fbUserDetails, LoginResponse response,String source){

    if(fbUserDetails!=null) {
      User savedUser = userRepository.findByEmailId((String) fbUserDetails.getEmail());
      String accessToken = "";
      if (savedUser != null) {
        response.setLoginSucces(true);
        JwtUser jwtUserDetails =
            (JwtUser) userDetailsService.loadUserByUsername(savedUser.getUsername());
        accessToken = jwtUtil.generateToken(jwtUserDetails);
        response.setUserName(savedUser.getUsername());
        response.setAccessToken(accessToken);
        return response;
      }

      User user = new User();
      user.setEmailId(fbUserDetails.getEmail());
      user.setFullName(fbUserDetails.getName());
      user.setUsername(fbUserDetails.getEmail());
      Set<Authority> auths = new HashSet<Authority>();
      Authority authority = authRepository.findByName(AuthorityName.ROLE_USER);
      if (authority == null) {
        authority = new Authority();
        authority.setName(AuthorityName.ROLE_USER);
      }
      auths.add(authority);
      user.setAuthorities(auths);
      user.setEnabled(true);
      user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
      if (source != null) {
        user.setSignUpSource(SignUpSourceType.valueOf(source));
      } else {
        user.setSignUpSource(SignUpSourceType.ANDROID);
      }
      user.setLoginType(LoginType.FACEBOOK);

      savedUser = userRepository.save(user);

      JwtUser userDetails =
          (JwtUser) userDetailsService.loadUserByUsername(savedUser.getUsername());
      accessToken = jwtUtil.generateToken(userDetails);
      response.setAccessToken(accessToken);
      emailService.sendEmailOnSignUp(savedUser);
      
    }else{
      response.setLoginSucces(false);
      response.setErrorMessage("Wrong Facebook credentials");
    }
    return response;
  }



  private FBAccessToken getFBAccessTokenFromCode(String code) {
    Map<String, String> urlparams = new HashMap<>();
    urlparams.put("client_id", APP_ID);
    urlparams.put("redirect_uri", REDIRECT_URI);
    urlparams.put("client_secret", APP_SECRET);
    urlparams.put("code", code);

    try {
      return restTemplate.getForObject(
          "https://graph.facebook.com/oauth/access_token?client_id={client_id}&code={code}&client_secret"
              + "={client_secret}&redirect_uri={redirect_uri}&scope=email",
          FBAccessToken.class, urlparams);
    }
    catch (Exception e)
    {
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
    Map<String, String> urlparams = new HashMap<>();
    urlparams.put("input_token", accessToken);
    urlparams.put("access_token", appAccessToken);
    return restTemplate.getForObject(
          "https://graph.facebook.com/debug_token?input_token={input_token}&access_token={access_token}",
          FBData.class, urlparams).getData();
  }


  public FBUserDetails getUserDetailsFromAccessToken(String accessToken) {

    Map<String, String> urlparams = new HashMap<>();
    urlparams.put("access_token", accessToken);
    urlparams.put("fields", "id,name,email");
    LOGGER.info("Retrieving user details with {} and {}", accessToken, urlparams);
    return restTemplate.getForObject("https://graph.facebook.com/v3.0/me/?access_token={access_token}&fields={fields}",
          FBUserDetails.class, urlparams);
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
    try{
      return new JSONObject(json).getString("access_token");
    }catch (JSONException e){
      return "";
    }
  }
}
