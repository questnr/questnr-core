package com.questnr.services.community;

import com.questnr.common.enums.PublishStatus;
import com.questnr.exceptions.InvalidInputException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.Community;
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

    public Community createCommunity(Community community, MultipartFile multipartFile) {
        if (community != null) {
            try {
                community.addMetadata();
                if (multipartFile != null) {

                }
                community.setOwnerUser(userCommonService.getUser());
                return communityRepository.saveAndFlush(community);
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
        try{
            if (!commonService.isNull(community.getAvatar())){
                try{
                    this.amazonS3Client.deleteFileFromS3BucketUsingPathToFile(this.communityCommonService.joinPathToFile(community.getAvatar(), communityId));
                }
                catch (Exception e){
                    LOGGER.error(CommunityService.class.getName() + " Exception Occurred. Couldn't able to delete resources of community on the cloud.");
                }
            }
            communityRepository.delete(community);
        }catch(Exception e){
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

    public List<User> getUsersFromCommunity(Long communityId){
        try{
            List<User> users = new ArrayList<>();
            users.addAll(communityCommonService.getCommunity(communityId).getUsers());
            return users;
        }catch (Exception e){
            LOGGER.error(CommunityService.class.getName() + " Exception Occurred");
            e.printStackTrace();
        }
        return null;
    }

    public List<Community> getCommunitiesFromLikeString(String communityString){
        try{
            return communityRepository.findByCommunityNameContaining(communityString);
        }catch (Exception e){
            LOGGER.error(CommunityService.class.getName() + " Exception Occurred");
            e.printStackTrace();
        }
        return null;
    }
}
