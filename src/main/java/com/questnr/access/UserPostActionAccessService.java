package com.questnr.access;

import com.questnr.common.enums.PostType;
import com.questnr.exceptions.AccessException;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.services.CommonService;
import com.questnr.services.PostActionService;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.user.UserCommonService;
import com.questnr.services.user.UserFollowerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPostActionAccessService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

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
    PostActionAccessService postActionAccessService;

    @Autowired
    UserFollowerService userFollowerService;

    @Autowired
    PostActionService postActionService;

    public User getAllPostsByUserId(Long userId) {
        User user = userCommonService.getUser(userId);
        if (user.equals(userCommonService.getUser()) || user.getPublic()) {
            return user;
        }
        return null;
    }

    public boolean hasAccessToPostModification(Long postId) {
        User user = userCommonService.getUser();
        // This rights only given to user actor of the post
        PostAction postAction = postActionRepository.findByPostActionIdAndUserActor(postId, user);
        return postActionAccessService.hasAccessToActionsOnPost(postAction);
    }

    public boolean hasAccessToPostDeletion(Long postId) {
        return this.hasAccessToPostModification(postId);
    }

    public PostAction createPollAnswerPost(Long postId) {
        User user = userCommonService.getUser();
        PostAction postAction = postActionService.getPostActionByIdAndType(postId, PostType.question);
        if (postAction.getUserActor().equals(user) || userFollowerService.existsUserFollower(postAction.getUserActor(), user)) {
            return postAction;
        }
        throw new AccessException("You are not following the user");
    }
}
