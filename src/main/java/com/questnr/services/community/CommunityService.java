package com.questnr.services.community;

import com.questnr.common.enums.PublishStatus;
import com.questnr.exceptions.InvalidInputException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityUser;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.CommonService;
import com.questnr.services.user.UserCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommunityService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AmazonS3Client amazonS3Client;

    @Autowired
    CommonService commonService;

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    CommunityAvatarService communityAvatarService;

    public Community createCommunity(Community community, MultipartFile multipartFile) {
        if (community != null) {
            try {
                community.setOwnerUser(userCommonService.getUser());community.addMetadata();
                Community communitySaved = communityRepository.saveAndFlush(community);
                if (multipartFile != null) {
                    communityAvatarService.uploadAvatar(communitySaved.getCommunityId(), multipartFile);
                    return communityCommonService.getCommunity(communitySaved.getCommunityId());
                }
                return communitySaved;
            } catch (Exception e) {
                LOGGER.error(CommunityService.class.getName() + " Exception Occurred");
            }
        } else {
            throw new InvalidInputException(Community.class.getName(), null, null);
        }
        return null;
    }

    public void deleteCommunity(long communityId) {
        Community community = communityCommonService.getCommunity(communityId);
        try {
            if (!commonService.isNull(community.getAvatar().getAvatarKey())) {
                try {
                    this.amazonS3Client.deleteFileFromS3BucketUsingPathToFile(community.getAvatar().getAvatarKey());
                } catch (Exception e) {
                    LOGGER.error(CommunityService.class.getName() + " Exception Occurred. Couldn't able to delete resources of community on the cloud.");
                }
            }
            communityRepository.delete(community);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Community not Found!");
        }
    }

    public List<Community> findAllCommunityNames() {
        List<Community> communities = null;
        try {
            communities = communityRepository.findAllByStatus(PublishStatus.publish);
        } catch (Exception e) {
            LOGGER.error(CommunityService.class.getName() + " Exception Occurred");
            e.printStackTrace();
        }
        return communities;
    }

    public List<User> getUsersFromCommunity(Long communityId) {
        try {
            List<User> users = communityCommonService.getCommunity(communityId).getUsers().stream().map(communityUser ->
                    communityUser.getUser()
            ).collect(Collectors.toList());
            return users;
        } catch (Exception e) {
            LOGGER.error(CommunityService.class.getName() + " Exception Occurred");
            e.printStackTrace();
        }
        return null;
    }

    public List<Community> getCommunitiesFromLikeString(String communityString) {
        try {
            return communityRepository.findByCommunityNameContaining(communityString);
        } catch (Exception e) {
            LOGGER.error(CommunityService.class.getName() + " Exception Occurred");
            e.printStackTrace();
        }
        return null;
    }
}