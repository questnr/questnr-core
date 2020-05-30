package com.questnr.services.user;

import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.UserRepository;
import com.questnr.requests.UpdatePasswordRequest;
import com.questnr.requests.UserUpdateRequest;
import com.questnr.responses.LoginResponse;
import com.questnr.responses.UpdatePasswordResponse;
import com.questnr.security.JwtTokenUtil;
import com.questnr.security.JwtUser;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.BaseService;
import com.questnr.services.CommonService;
import com.questnr.services.EmailService;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.community.CommunityService;
import com.questnr.util.EncryptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AmazonS3Client amazonS3Client;

    @Autowired
    EmailService emailService;

    @Autowired
    CommonService commonService;

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    BaseService baseService;

    public void deleteUser(Long userId) {
        User user = userCommonService.getUser();
        if (!Objects.equals(user.getUserId(), userId)) {
            throw new ResourceNotFoundException("User not Found!");
        }
        try {
            if (user.getAvatar() != null) {
                try {
                    this.amazonS3Client.deleteFileFromS3BucketUsingPathToFile(user.getAvatar().getAvatarKey());
                } catch (Exception e) {
                    LOGGER.error(Community.class.getName() + " Exception Occurred. Couldn't able to delete resources of community on the cloud.");
                }
            }
            user.getAuthorities().clear();
            userRepository.delete(user);
        } catch (Exception e) {
            throw new ResourceNotFoundException("User not Found!");
        }
    }

    public LoginResponse updateUser(UserUpdateRequest userUpdateRequest){
        User user = userCommonService.getUser();
        try{
            user.setFirstName(userUpdateRequest.getFirstName());
            user.setLastName(userUpdateRequest.getLastName());
            user.setUsername(userUpdateRequest.getUsername());
            user.setDob(userUpdateRequest.getDob());
            user.setBio(userUpdateRequest.getBio());
            User savedUser = userRepository.save(user);
            return baseService.createSuccessLoginResponse(savedUser);
        }catch (Exception e) {
            LOGGER.error(CommunityService.class.getName() + " Exception Occurred");
        }
        return null;
    }

    public UpdatePasswordResponse updatePassword(UpdatePasswordRequest updatePasswordRequest) {

        UpdatePasswordResponse response = new UpdatePasswordResponse();
        if (updatePasswordRequest != null) {
            String encryptedPassword = EncryptionUtils.encryptPassword(updatePasswordRequest.getPassword());
            String userName = jwtTokenUtil.getUsernameFromToken(updatePasswordRequest.getResetToken());
            User user = userRepository.findByUsername(userName);
            user.setPassword(encryptedPassword);
            userRepository.save(user);
            response.setUpdateSuccess(true);
            response.setUsername(userName);
        } else {
            throw new InvalidRequestException("Invalid request!");
        }
        return response;
    }

    public boolean validatePasswordResetToken(String token) {
        String userName = jwtTokenUtil.getUsernameFromToken(token);
        User savedUser = userRepository.findByUsername(userName);

        if (savedUser != null) {
            JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(savedUser.getUsername());
            return jwtTokenUtil.validateResetToken(token, userDetails);
        }
        return false;
    }
}
