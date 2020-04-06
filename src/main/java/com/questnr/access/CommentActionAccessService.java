package com.questnr.access;

import com.questnr.model.entities.CommentAction;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.CommentActionRepository;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.services.CommonService;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentActionAccessService {

    @Autowired
    CommonService commonService;

    @Autowired
    CommentActionRepository commentActionRepository;

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    private CommunityPostActionAccessService communityPostActionAccessService;

    private boolean isUserOwnerOfComment(User user, CommentAction commentAction) {
        return user.equals(commentAction.getUserActor());
    }

    public boolean hasAccessToCommentCreation(Long postId) {
        PostAction postAction = postActionRepository.findByPostActionId(postId);
        Long communityId = commonService.getCommunityId(postAction);
        return communityId == null || communityPostActionAccessService.hasAccessToPostBaseService(communityId);
    }

    public boolean hasAccessToCommentDeletion(Long postId, Long commentId) {
        User user = userCommonService.getUser();
        PostAction postAction = postActionRepository.findByPostActionId(postId);

        // If the user is the owner of the post
        if (communityPostActionAccessService.isUserOwnerOfPost(user, postAction))
            return true;

        CommentAction commentAction = commentActionRepository.findByCommentActionId(commentId);

        // If the user is the owner of the comment
        if (this.isUserOwnerOfComment(user, commentAction))
            return true;

        return this.hasAccessToCommentCreation(postId);
    }

}
