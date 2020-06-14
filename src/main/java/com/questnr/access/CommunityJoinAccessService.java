package com.questnr.access;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityUser;
import com.questnr.model.entities.User;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityJoinAccessService {

    @Autowired
    CommunityAccessService communityAccessService;

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    UserCommonService userCommonService;

    public boolean hasAccessToJoinCommunity(Long communityId) {
        return true;
    }

    public boolean hasAccessToInviteUser(Long communityId) {
        return true;
    }

    public boolean revokeJoinFromUser(Long communityId, Long userId) {
        Community community = communityCommonService.getCommunity(communityId);
        if (community.getOwnerUser().getUserId().equals(userCommonService.getUserId())) {
            return true;
        } else if (userId.equals(userCommonService.getUserId())) {
            return true;
        }
        return false;
    }

    public User getJoinedCommunityList(Long userId) {
        // @Todo: isPublic
        return userCommonService.getUser(userId);
    }

    public User getCommunityInvitationList(Long userId) {
        // @Todo: isPublic
        return userCommonService.getUser(userId);
    }

    public CommunityUser getUserListToInvite(Long communityId){
        User user = userCommonService.getUser();
        Community community = communityCommonService.getCommunity(communityId);
        if(communityAccessService.isUserMemberOfCommunity(user, community)){
            CommunityUser communityUser = new CommunityUser();
            communityUser.setCommunity(community);
            communityUser.setUser(user);
            return communityUser;
        }
        return null;
    }
}
