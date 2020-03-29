package com.questnr.services.user;

import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.UserRepository;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.CommonService;
import com.questnr.services.community.CommunityCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AmazonS3Client amazonS3Client;

    @Autowired
    CommonService commonService;

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserCommonService userCommonService;


    public void deleteUser(Long userId) {
        User user = userCommonService.getUser();
        if(!Objects.equals(user.getUserId(), userId)){
            throw new ResourceNotFoundException("User not Found!");
        }
        try{
            if (!commonService.isNull(user.getAvatar())){
                try{
                    this.amazonS3Client.deleteFileFromS3BucketUsingPathToFile(this.userCommonService.joinPathToFile(user.getAvatar()));
                }
                catch (Exception e){
                    LOGGER.error(Community.class.getName() + " Exception Occurred. Couldn't able to delete resources of community on the cloud.");
                }
            }
            userRepository.delete(user);
        }catch(Exception e){
            throw new ResourceNotFoundException("User not Found!");
        }
    }
}
