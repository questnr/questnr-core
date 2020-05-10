package com.questnr.services.community;

import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.Avatar;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.responses.ResourceStorageData;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class CommunityAvatarService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AmazonS3Client amazonS3Client;

    @Autowired
    CommonService commonService;

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    CommunityRepository communityRepository;

    public String uploadAvatar(long communityId, MultipartFile multipartFile) {
        try {
            File file = commonService.convertMultiPartToFile(multipartFile);
            if (commonService.checkIfFileIsImage(file)) {
                ResourceStorageData resourceStorageData = this.amazonS3Client.uploadFile(file, communityId);
                try {
                    Community community = communityCommonService.getCommunity(communityId);
                    Avatar avatar = new Avatar();
                    avatar.addMetadata();
                    avatar.setAvatarKey(resourceStorageData.getKey());
                    community.setAvatar(avatar);
                    communityRepository.save(community);
                } catch (Exception e) {
                    LOGGER.error(CommunityAvatarService.class.getName() + " Exception Occurred");
                    throw new InvalidRequestException("Error occurred. Please, try again!");
                }
                return resourceStorageData.getUrl();
            }else{
                throw new InvalidRequestException("Avatar can not be of video format");
            }
        } catch (IOException ex) {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public String getAvatar(String communitySlug) {
        Community community = communityCommonService.getCommunity(communitySlug);
        if (!commonService.isNull(community.getAvatar().getAvatarKey())) {
            try {
                return this.amazonS3Client.getS3BucketUrl(community.getAvatar().getAvatarKey());
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public void deleteAvatar(long communityId) {
        Community community = communityCommonService.getCommunity(communityId);
        if (!commonService.isNull(community.getAvatar().getAvatarKey())) {
            try {
                this.amazonS3Client.deleteFileFromS3BucketUsingPathToFile(community.getAvatar().getAvatarKey());
            } catch (Exception e) {
                throw new ResourceNotFoundException("User avatar not found!");
            }
            try {
                community.setAvatar(null);
                communityRepository.save(community);
            } catch (Exception e) {
                LOGGER.error(User.class.getName() + " Exception Occurred");
                throw new InvalidRequestException("Error occurred. Please, try again!");
            }
        }
    }

    public byte[] getAvatarInBytes(String communitySlug) {
        Community community = communityCommonService.getCommunity(communitySlug);
        if (!commonService.isNull(community.getAvatar().getAvatarKey())) {
            return this.amazonS3Client.getFile(community.getAvatar().getAvatarKey());
        }
        return null;
    }
}
