package com.questnr.access;

import com.questnr.exceptions.InvalidRequestException;
import com.questnr.model.entities.SharedPostAction;
import com.questnr.model.repositories.SharePostActionRepository;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SharePostActionAccessService {
    @Autowired
    UserCommonService userCommonService;

    @Autowired
    SharePostActionRepository sharePostActionRepository;

    @Autowired
    PostActionAccessService postActionAccessService;

    public boolean sharePost(Long postId) {
        return postActionAccessService.hasAccessToActionsOnPost(postId);
    }

    public SharedPostAction deleteSharedPost(Long sharedPostId) {
        SharedPostAction sharedPostAction = sharePostActionRepository.findBySharedPostActionId(sharedPostId);
        if (sharedPostAction != null) {
            if (sharedPostAction.getUserActor().equals(userCommonService.getUser())) {
                return sharedPostAction;
            }
            return null;
        } else {
            throw new InvalidRequestException("Shared post not found!");
        }
    }

}
