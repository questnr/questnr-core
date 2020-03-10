package com.questnr.services;

import com.questnr.model.entities.PostVisit;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.UserRepository;
import com.questnr.responses.UserAvatarStorageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserAvatarService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AmazonS3Client amazonS3Client;

    @Autowired
    CommonUserService commonUserService;

    @Autowired
    UserRepository userRepository;

    public String uploadAvatar(MultipartFile file) {
        UserAvatarStorageData userAvatarStorageData = this.amazonS3Client.uploadFile(file);
        try {
            User user = commonUserService.getUser();
            user.setAvatar(userAvatarStorageData.getFileName());
            userRepository.save(user);
        } catch (Exception e) {
            LOGGER.error(PostVisit.class.getName() + " Exception Occurred");
        }
        return userAvatarStorageData.getUrl();
    }

    public ResponseEntity<?> deleteAvatar() {
        User user = commonUserService.getUser();
        if (user.getAvatar() != null || user.getAvatar() != "")
            this.amazonS3Client.deleteFileFromS3BucketUsingPathToFile(commonUserService.joinPathToFile(user.getAvatar()));
        try {
            user.setAvatar("");
            userRepository.save(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            LOGGER.error(PostVisit.class.getName() + " Exception Occurred");
        }
        return ResponseEntity.notFound().build();
    }

    public byte[] getAvatar() {
        User user = commonUserService.getUser();
        if (user.getAvatar() != null || user.getAvatar() != "") {
            return this.amazonS3Client.getFile(commonUserService.joinPathToFile(user.getAvatar()));
        }
        return null;
    }
}
