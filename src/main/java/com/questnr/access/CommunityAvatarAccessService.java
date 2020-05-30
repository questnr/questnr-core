package com.questnr.access;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.services.CommonService;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityAvatarAccessService {

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    CommonService commonService;

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    CommunityAccessService communityAccessService;

    @Autowired
    CommunityPostActionAccessService communityPostActionAccessService;

    @Autowired
    CommunityRepository communityRepository;


    public boolean hasAccessToCommunityAvatar(Long communityId) {
        return communityAccessService.hasAccessToCommunityBasic(communityId);
    }

    public boolean hasAccessToCommunityCreation() {
        return true;
    }

    public Community hasAccessToCommunityUpdate(Long communityId) {
        Community community = communityRepository.findByCommunityId(communityId);
        if(communityAccessService.isUserOwnerOfCommunity(userCommonService.getUser(),
               community)){
            return community;
        }
        return null;
    }

    public User getCommunityListOfUser(Long userId) {
        return userCommonService.getUser(userId);
//        if(userId.equals(userCommonService.getUserId())) {
//
//        }
    }

    public boolean hasAccessToCommunityDeletion() {
        return true;
    }

    public boolean hasAccessToGetCommunityUsers() {
        return true;
    }
}
