package com.questnr.access;

import com.questnr.exceptions.InvalidRequestException;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityUser;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.CommunityInvitedUserRepository;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CommunityJoinAccessService {

    @Autowired
    CommunityAccessService communityAccessService;

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    CommunityInvitedUserRepository communityInvitedUserRepository;

    final int MAX_NUMBER_OF_EMAIL_PER_WEEK = 1;

    final int PER_DAYS = 2 * 86400 * 1000;

    public boolean hasAccessToJoinCommunity(Long communityId) {
        return true;
    }

    public boolean hasAccessToInviteUser(Long communityId, String userEmail) {
        return this.hasAccessToInviteUser(communityId, userCommonService.getUser(userEmail));
    }

    public boolean hasAccessToInviteUser(Long communityId, Long userId) {
        return this.hasAccessToInviteUser(communityId, userCommonService.getUser(userId));
    }

    public boolean hasAccessToInviteUser(Long communityId, User user) {
        User userActor = userCommonService.getUser();
        if(communityCommonService.isUserOwnerOfCommunity(userActor, communityId)){
            Community community = communityCommonService.getCommunity(communityId);
            Date endingDate = new Date();
            Date startingDate = new Date(endingDate.getTime() - PER_DAYS);
            int numberOfHasBeenSent = communityInvitedUserRepository.countAllByCommunityAndUserActorAndUserAndCreatedAtBetween(
                    community,
                    userActor,
                    user,
                    startingDate,
                    endingDate
            );
            if(numberOfHasBeenSent >= MAX_NUMBER_OF_EMAIL_PER_WEEK) {
                throw new InvalidRequestException("You can sent invitation to the user two per week!");
            }
            return true;
        }
        return false;
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
        if(communityCommonService.isUserMemberOfCommunity(user, community)){
            CommunityUser communityUser = new CommunityUser();
            communityUser.setCommunity(community);
            communityUser.setUser(user);
            return communityUser;
        }
        return null;
    }
}
