package com.questnr.access;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommunityAccessService {
    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    UserCommonAccessService userCommonAccessService;

    @Autowired
    CommunityCommonAccessService communityCommonAccessService;

    public Community isUserOwnerOfCommunity(Long communityId) {
        return this.isUserOwnerOfCommunity(userCommonService.getUser(), communityId);
    }

    public Community isUserOwnerOfCommunity(User user, Long communityId) {
        Community community = communityCommonService.getCommunity(communityId);
        if(this.isUserMemberOfCommunity(user, community)){
            return community;
        }
        return null;
    }

    public boolean isUserOwnerOfCommunity(User user, Community community) {
        return user.equals(community.getOwnerUser());
    }

    public boolean isUserMemberOfCommunity(User user, Community community) {
        // If user is the owner of the community
        if (this.isUserOwnerOfCommunity(user, community)) {
            return true;
        }
        // If the user is a member of the community
        List<Long> userIdList = community.getUsers().stream().map(communityUser ->
                communityUser.getUser().getUserId()
        ).collect(Collectors.toList());
        return userIdList.contains(user.getUserId());
    }

    public boolean hasAccessToCommunityBasic(Long communityId){
        User user = userCommonService.getUser();
        Community community = communityCommonService.getCommunity(communityId);
        return this.isUserMemberOfCommunity(user, community);
    }


    public boolean hasAccessToCommunityCreation() {
        return true;
    }

    public Community hasAccessToCommunityUpdate(Long communityId) {
        Community community = communityRepository.findByCommunityId(communityId);
        if(this.isUserOwnerOfCommunity(userCommonService.getUser(),
                community)){
            return community;
        }
        return null;
    }

    public User getCommunityListOfUser(Long userId) {
        User user = userCommonService.getUser(userId);
        if(userCommonAccessService.isUserAccessibleWithPrivacy(user)){
            return user;
        }
        return null;
    }

    public boolean hasAccessToCommunityDeletion() {
        return true;
    }

    public boolean hasAccessToCommunity(String communitySlug) {
        return communityCommonAccessService.isCommunityAccessibleWithPrivacy(communityCommonService.getCommunity(communitySlug));
    }

    public boolean hasAccessToGetCommunityUsers(String communitySlug) {
        return this.hasAccessToCommunity(communitySlug);
    }
}
