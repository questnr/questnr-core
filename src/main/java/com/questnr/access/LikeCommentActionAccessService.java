package com.questnr.access;

import com.questnr.model.repositories.CommentActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeCommentActionAccessService {
    
    @Autowired
    CommentActionRepository commentActionRepository;

    @Autowired
    PostActionAccessService postActionAccessService;

    public boolean hasAccessToCommentLikeAction(Long commentId) {
        return postActionAccessService.hasAccessToActionsOnPost(commentActionRepository.findByCommentActionId(commentId).getPostAction());
    }
}
