package com.questnr.access;

import com.questnr.access.community.CommunityCommonAccessService;
import com.questnr.access.user.UserCommonAccessService;
import com.questnr.common.enums.PostEditorType;
import com.questnr.common.enums.PostType;
import com.questnr.exceptions.AccessException;
import com.questnr.exceptions.InvalidRequestException;
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

    public void commonPostAccessStrategy(PostAction postAction) {
        if (postAction.getPostEditorType() == PostEditorType.blog) {
            throw new InvalidRequestException("Blogs can not be edited");
        }
    }

    public PostAction createPollAnswerPost(Long postId) {
//        User user = userCommonService.getUser();
        PostAction postAction = postActionService.getPostActionByIdAndType(postId, PostType.question);
        if (postActionService.isPostActionBelongsToCommunity(postAction)) {
            if (communityCommonAccessService.isCommunityAccessibleWithPrivacy(postAction.getCommunity())) {
                return postAction;
            } else {
                throw new AccessException("You don't have permission to answer this question");
            }
        } else {
            return postAction;
        }
        // uncomment to restrict only answers from users who are followers of the owner of the post
//        if (postAction.getUserActor().equals(user) || userFollowerService.existsUserFollower(postAction.getUserActor(), user)) {
//            return postAction;
//        }
//        throw new AccessException("You are not following the user");
    }
}
