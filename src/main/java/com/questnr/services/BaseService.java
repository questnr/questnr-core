package com.questnr.services;

import com.questnr.common.enums.AuthorityName;
import com.questnr.common.enums.SignUpSourceType;
import com.questnr.common.enums.UserPrivacy;
import com.questnr.exceptions.AlreadyExistsException;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.model.entities.Authority;
import com.questnr.model.entities.User;
import com.questnr.model.entities.UserSecondaryDetails;
import com.questnr.model.mapper.UserMapper;
import com.questnr.model.repositories.AuthorityRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.requests.LoginRequest;
import com.questnr.requests.UserRequest;
import com.questnr.responses.LoginResponse;
import com.questnr.responses.ResetPasswordResponse;
import com.questnr.security.JwtTokenUtil;
import com.questnr.security.JwtUser;
import com.questnr.services.user.UserCommonService;
import com.questnr.util.EncryptionUtils;
import com.questnr.util.SecureRandomService;
import org.apache.commons.text.WordUtils;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BaseService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommonService commonService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    SecureRandomService secureRandomService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserCommonService userCommonService;

    public BaseService() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    public String createUsername(String fullName) {
        String username = CommonService.removeSpecialCharactersWithWhiteSpace(fullName.toLowerCase());
        Long timestamp = new Date().getTime();
        String randString = timestamp.toString();
        String makingUsername;
        int appendNum = 0;
        do {
            makingUsername = username + randString.substring(randString.length() - appendNum);
            appendNum++;
        } while (this.checkIfUsernameIsTaken(makingUsername));
        return makingUsername;
    }

    public String createSlug(String fullName) {
        List<String> chucks = Arrays.asList(fullName.toLowerCase().split("\\s"));
        String slug = CommonService.removeSpecialCharacters(String.join("-", chucks));
        Long timestamp = new Date().getTime();
        String randString = timestamp.toString();
        return slug + "-" + randString.substring(randString.length() > 10 ? randString.length() - 10 : randString.length());
    }

    public User createUserFromSocialLogin(User user, String source) {
        this.processUserInformation(user);

        try {
            user.setSignUpSource(SignUpSourceType.valueOf(source));
        } catch (Exception e) {
            user.setSignUpSource(SignUpSourceType.WEB);
        }
        return userRepository.saveAndFlush(user);
    }

    public LoginResponse createSuccessLoginResponse(User savedUser) {
        LoginResponse response = new LoginResponse();
        response.setUserName(savedUser.getUsername());
        response.setLoginSuccess(true);
        JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(savedUser.getUsername());
        response.setAccessToken(jwtTokenUtil.generateToken(userDetails));
        UserSecondaryDetails userSecondaryDetails = savedUser.getUserSecondaryDetails();
        if (userSecondaryDetails != null) {
            if (userSecondaryDetails.getLoggedInCount() >= 0)
                response.setFirstAttempt(userSecondaryDetails.getLoggedInCount() == 0);
            response.setCommunitySuggestion(userSecondaryDetails.isCommunitySuggestion());
        }else{
            response.setFirstAttempt(true);
            response.setCommunitySuggestion(true);
        }
        return response;
    }

    public LoginResponse createErrorLoginResponse() {
        return this.createErrorLoginResponse("Wrong credentials");
    }

    public LoginResponse createErrorLoginResponse(String errorMessage) {
        LoginResponse response = new LoginResponse();
        response.setLoginSuccess(false);
        response.setErrorMessage(errorMessage);
        return response;
    }

    public Set<Authority> createAuthoritySet() {
        Set<Authority> authoritySet = new HashSet<>();
        Authority authority = authorityRepository.findByName(AuthorityName.ROLE_USER);
        if (authority == null) {
            authority = new Authority();
            authority.setName(AuthorityName.ROLE_USER);
        }
        authoritySet.add(authority);
        return authoritySet;
    }

    private User processUserInformation(User user) {

        if (!(commonService.isNull(user.getFirstName()) || commonService.isNull(user.getLastName()))) {
            user.setSlug(this.createSlug(user.getFirstName() + " " + user.getLastName()));
        } else {
            user.setSlug(this.createSlug(user.getUsername()));
        }

        user.setFirstName(commonService.titleCase(user.getFirstName()));
        user.setLastName(commonService.titleCase(user.getLastName()));

        user.setEnabled(true);
        user.setAuthorities(this.createAuthoritySet());
        user.addMetadata();
        user.setUserPrivacy(UserPrivacy.pub);
        final char[] delimiters = {' '};

        user.setFirstName(WordUtils.capitalizeFully(user.getFirstName(), delimiters));
        user.setLastName(WordUtils.capitalizeFully(user.getLastName(), delimiters));
        user.setAgree(true);
        return user;
    }

    public LoginResponse signUp(UserRequest userRequest) {
        User user = userMapper.fromUserRequest(userRequest);
        if (user != null && user.getEmailId() != null && user.getUsername() != null) {
            try {
                this.checkIfEmailIsTakenWithException(user.getEmailId());
                this.checkIfUsernameIsTakenWithException(user.getUsername());
            } catch (AlreadyExistsException e) {
                return this.createErrorLoginResponse(e.getMessage());
            }
        }
        try {
            assert user != null;
            if (CommonService.isNull(user.getPassword())) {
                throw new InvalidRequestException("Password is mandatory.");
            }
        } catch (NullPointerException ex) {
            throw new InvalidRequestException("Password is mandatory.");
        }

        this.processUserInformation(user);

        user.setEmailVerified(true);

        user.setSignUpSource(SignUpSourceType.WEB);

        // For encrypting the password
        String encPassword = EncryptionUtils.encryptPassword(user.getPassword());
        if (encPassword != null) {
            user.setPassword(encPassword);
        }

        User savedUser = userRepository.saveAndFlush(user);
        this.emailService.sendEmailOnSignUp(user);
        return this.createSuccessLoginResponse(savedUser);
    }

    public LoginResponse login(LoginRequest request) {
        if (request == null) {
            return this.createErrorLoginResponse();
        } else {
            User savedUser = userRepository.findByEmailId(request.getEmailId());
            if (savedUser == null) {
                savedUser = userRepository.findByUsername(request.getEmailId());
            }
            if (savedUser != null) {
                if (checkValidLogin(savedUser, request.getPassword())) {
                    return this.createSuccessLoginResponse(savedUser);
                } else {
                    return this.createErrorLoginResponse();
                }
            } else {
                return this.createErrorLoginResponse("User doesn't exist. Please sign up.");
            }
        }
    }

    private boolean checkValidLogin(User user, String password) {
        String userPassword = user.getPassword();
        return userPassword != null && EncryptionUtils.isValidPassword(password, userPassword);
    }

    public boolean checkIfUsernameIsTakenWithException(String username) {
        if (this.checkIfUsernameIsTaken(username)) {
            throw new AlreadyExistsException("Username is already taken");
        }
        return false;
    }

    public boolean checkIfOtherUsernameIsTaken(User user, String username) {
        return userRepository.existsByOtherUsername(user.getUserId(), username);
    }

    public boolean checkIfOtherUsernameIsTakenWithException(String username) {
        User user = new User();
        try {
            user = userCommonService.getUser();
        } catch (Exception e) {
        }
        if (CommonService.isNull(user.getUsername())) {
            if (this.checkIfUsernameIsTaken(username)) {
                throw new AlreadyExistsException("Username is already taken");
            }
        } else {
            if (this.checkIfOtherUsernameIsTaken(user, username)) {
                throw new AlreadyExistsException("Username is already taken");
            }
        }
        return false;
    }

    private boolean checkIfEmailIsTaken(String email) {
        return userRepository.existsByEmailId(email);
    }

    public boolean checkIfUsernameIsTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean checkIfEmailIsTakenWithException(String email) {
        if (this.checkIfEmailIsTaken(email)) {
            throw new AlreadyExistsException("User already exists");
        }
        return false;
    }


    public ResetPasswordResponse generatePasswordResetToken(String emailID) {

        ResetPasswordResponse response = new ResetPasswordResponse();
        {
            User savedUser = userRepository.findByEmailId(emailID);
            if (savedUser != null) {
                JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(savedUser.getUsername());
                String passwordResetToken = jwtTokenUtil.generatePasswordResetToken(userDetails);
                if (passwordResetToken != null && !CommonService.isNull(passwordResetToken)) {
                    response.setSuccess(true);
                    emailService.sendPasswordRequestEmail(emailID, passwordResetToken,
                            savedUser.getUsername());
                } else {
                    response.setSuccess(true);
                    response.setErrorMessage("Error occurred. Please try again");
                }
            } else {
                response.setSuccess(false);
                response.setErrorMessage("Email Id is not registered with us.");
            }
        }
        return response;
    }
}
