package com.questnr.access.community;

import com.questnr.access.user.UserCommonAccessService;
import com.questnr.common.enums.CommunitySuggestionDialogActionType;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.User;
import com.questnr.model.entities.UserSecondaryDetails;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public boolean hasAccessToCommunityBasic(Long communityId) {
        User user = userCommonService.getUser();
        Community community = communityCommonService.getCommunity(communityId);
        return communityCommonService.isUserMemberOfCommunity(user, community);
    }


    public boolean hasAccessToCommunityCreation() {
        return true;
    }

    public Community hasAccessToCommunityUpdate(Long communityId) {
        Community community = communityRepository.findByCommunityId(communityId);
        if (communityCommonService.isUserOwnerOfCommunity(userCommonService.getUser(),
                community)) {
            return community;
        }
        return null;
    }

    public User getCommunityListOfUser(Long userId) {
        User user = userCommonService.getUser(userId);
        if (userCommonAccessService.isUserAccessibleWithPrivacy(user)) {
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

    public boolean hasAccessToCommunity(Long communityId) {
        return communityCommonAccessService.isCommunityAccessibleWithPrivacy(communityCommonService.getCommunity(communityId));
    }

    public boolean hasAccessToGetCommunityUsers(String communitySlug) {
        return this.hasAccessToCommunity(communitySlug);
    }

    public boolean toCommunitySuggestionsForGuide() {
        UserSecondaryDetails userSecondaryDetails = userCommonService.getUser().getUserSecondaryDetails();
        if (userSecondaryDetails != null) {
            return userSecondaryDetails.getCommunitySuggestion() !=
                    CommunitySuggestionDialogActionType.completed;
        }
        return true;
    }
}
