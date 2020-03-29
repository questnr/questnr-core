package com.questnr.services.access;

import com.questnr.model.entities.CommentAction;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.CommentActionRepository;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.services.CommonService;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
    private PostActionAccessService postActionAccessService;

    public boolean isUserOwnerOfComment(User user, CommentAction commentAction) {
        if (Objects.equals(user.getUserId(), commentAction.getUserActor().getUserId())) {
            return true;
        }
        return false;
    }

    public boolean hasAccessToCommentCreation(Long postId) {
        PostAction postAction = postActionRepository.findByPostActionId(postId);
        Long communityId = commonService.getCommunityId(postAction);
        if (communityId == null || postActionAccessService.hasAccessToPostBaseService(communityId))
            return true;
        return false;
    }

    public boolean hasAccessToCommentDeletion(Long postId, Long commentId) {
        User user = userCommonService.getUser();
        PostAction postAction = postActionRepository.findByPostActionId(postId);

        // If the user is the owner of the post
        if(postActionAccessService.isUserOwnerOfPost(user, postAction))
            return true;

        CommentAction commentAction = commentActionRepository.findByCommentActionId(commentId);

        // If the user is the owner of the comment
        if(this.isUserOwnerOfComment(user, commentAction))
            return true;

        return this.hasAccessToCommentCreation(postId);
    }

}
