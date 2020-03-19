package com.questnr.services.community;

import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.responses.AvatarStorageData;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public String uploadAvatar(long communityId, MultipartFile file) {
        AvatarStorageData avatarStorageData = this.amazonS3Client.uploadFileForCommunity(file, communityId);
        try {
            Community community = communityCommonService.getCommunity(communityId);
            community.setAvatar(avatarStorageData.getFileName());
            communityRepository.save(community);
        } catch (Exception e) {
            LOGGER.error(CommunityAvatarService.class.getName() + " Exception Occurred");
        }
        return avatarStorageData.getUrl();
    }

    public String getUserAvatar(long communityId) {
        Community community = communityCommonService.getCommunity(communityId);
        if (!commonService.isNull(community.getAvatar())) {
            return this.amazonS3Client.getS3BucketUrl(communityCommonService.joinPathToFile(community.getAvatar(), communityId));
        }
        return null;
    }

    public void deleteAvatar(long communityId) {
        Community community = communityCommonService.getCommunity(communityId);
        if (!commonService.isNull(community.getAvatar())) {
            try {
                this.amazonS3Client.deleteFileFromS3BucketUsingPathToFile(communityCommonService.joinPathToFile(community.getAvatar(), communityId));
            } catch (Exception e) {
                throw new ResourceNotFoundException("User avatar not found!");
            }
            try {
                community.setAvatar(null);
                communityRepository.save(community);
            } catch (Exception e) {
                LOGGER.error(User.class.getName() + " Exception Occurred");
            }
        }
    }

    public byte[] getAvatar(long communityId) {
        Community community = communityCommonService.getCommunity(communityId);
        if (!commonService.isNull(community.getAvatar())) {
            return this.amazonS3Client.getFile(communityCommonService.joinPathToFile(community.getAvatar(), communityId));
        }
        return null;
    }
}
