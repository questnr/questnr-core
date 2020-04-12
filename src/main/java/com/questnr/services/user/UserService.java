package com.questnr.services.user;

import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.UserRepository;
import com.questnr.responses.ResetPasswordResponse;
import com.questnr.security.JwtTokenUtil;
import com.questnr.security.JwtUser;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.CommonService;
import com.questnr.services.EmailService;
import com.questnr.services.community.CommunityCommonService;
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

    public User getUserByUsername(String username){
        User user = userRepository.findByUsername(username);
        if(user != null){
            return user;
        }
        throw new ResourceNotFoundException("User not found!");
    }


    public void deleteUser(Long userId) {
        User user = userCommonService.getUser();
        if(!Objects.equals(user.getUserId(), userId)){
            throw new ResourceNotFoundException("User not Found!");
        }
        try{
            if (user.getAvatar() != null){
                try{
                    this.amazonS3Client.deleteFileFromS3BucketUsingPathToFile(user.getAvatar().getAvatarKey());
                }
                catch (Exception e){
                    LOGGER.error(Community.class.getName() + " Exception Occurred. Couldn't able to delete resources of community on the cloud.");
                }
            }
            user.getAuthorities().clear();
            userRepository.delete(user);
        }catch(Exception e){
            throw new ResourceNotFoundException("User not Found!");
        }
    }

    public ResetPasswordResponse generatePasswordResetToken(String emailID) {

        ResetPasswordResponse response = new ResetPasswordResponse();
        {
            User savedUser = userRepository.findByEmailId(emailID);
            if (savedUser != null) {
                JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(savedUser.getUsername());
                String passwordResetToken = jwtTokenUtil.generatePasswordResetToken(userDetails);
                if (passwordResetToken != null && !commonService.isNull(passwordResetToken)) {
                    response.setSuccess(true);
                    emailService.sendPasswordRequestEmail(emailID, passwordResetToken,
                            savedUser.getFullName());
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
