package com.questnr.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeActionAccessService {
    
    @Autowired
    PostActionAccessService postActionAccessService;

    public boolean hasAccessToPostLikeAction(Long postId) {
        return postActionAccessService.hasAccessToActionsOnPost(postId);
    }

    public boolean hasAccessToPostLikeAction(String postString) {
        return postActionAccessService.hasAccessToActionsOnPost(postString);
    }
}
