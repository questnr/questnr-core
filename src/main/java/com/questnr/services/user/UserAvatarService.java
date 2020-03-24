package com.questnr.services.user;

import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.UserRepository;
import com.questnr.responses.AvatarStorageData;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserAvatarService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AmazonS3Client amazonS3Client;

    @Autowired
    CommonService commonService;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    UserRepository userRepository;

    public String uploadAvatar(MultipartFile file) {
        AvatarStorageData avatarStorageData = this.amazonS3Client.uploadFile(file);
        try {
            User user = userCommonService.getUser();
            user.setAvatar(avatarStorageData.getKey());
            userRepository.save(user);
        } catch (Exception e) {
            LOGGER.error(UserAvatarService.class.getName() + " Exception Occurred");
        }
        return avatarStorageData.getUrl();
    }

    public String getUserAvatar(){
        User user = userCommonService.getUser();
        if (!commonService.isNull(user.getAvatar())) {
            return this.amazonS3Client.getS3BucketUrl(userCommonService.joinPathToFile(user.getAvatar()));
        }
        return null;
    }

    public void deleteAvatar() {
        User user = userCommonService.getUser();
        if (!commonService.isNull(user.getAvatar())) {
            try {
                this.amazonS3Client.deleteFileFromS3BucketUsingPathToFile(userCommonService.joinPathToFile(user.getAvatar()));
            } catch (Exception e) {
                throw new ResourceNotFoundException("User avatar not found!");
            }
            try {
                user.setAvatar(null);
                userRepository.save(user);
            } catch (Exception e) {
                LOGGER.error(UserAvatarService.class.getName() + " Exception Occurred");
            }
        }
    }

    public byte[] getAvatar() {
        User user = userCommonService.getUser();
        if (!commonService.isNull(user.getAvatar())) {
            return this.amazonS3Client.getFile(userCommonService.joinPathToFile(user.getAvatar()));
        }
        return null;
    }
}
