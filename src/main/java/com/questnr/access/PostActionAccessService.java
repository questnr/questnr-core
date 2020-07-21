package com.questnr.access;

import com.questnr.access.community.CommunityCommonAccessService;
import com.questnr.access.user.UserCommonAccessService;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.services.PostActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostActionAccessService {


    @Autowired
    CommunityCommonAccessService communityCommonAccessService;

    @Autowired
    PostActionService postActionService;

    @Autowired
    UserCommonAccessService userCommonAccessService;

    public boolean isUserOwnerOfPost(User user, PostAction postAction) {
        return user.equals(postAction.getUserActor());
    }

    public boolean hasAccessToActionsOnPost(Long postId) {
        return this.hasAccessToActionsOnPost(postActionService.getPostActionById(postId));
    }

    public boolean hasAccessToActionsOnPost(String postSlug) {
        return this.hasAccessToActionsOnPost(postActionService.getPostActionFromSlug(postSlug));
    }

    public boolean hasAccessToActionsOnPost(PostAction postAction) {
        if (postActionService.isPostActionBelongsToCommunity(postAction)) {
            return communityCommonAccessService.isCommunityAccessibleWithPrivacy(postAction.getCommunity());
        } else return userCommonAccessService.isUserAccessibleWithPrivacy(postAction.getUserActor());
    }
}
