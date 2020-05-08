package com.questnr.services;

import com.questnr.exceptions.InvalidRequestException;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.SharedPostAction;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.model.repositories.SharePostActionRepository;
import com.questnr.services.user.UserCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SharePostActionService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    SharePostActionRepository sharePostActionRepository;

    public void sharePost(Long postId) {
        // @Todo return PostActionDTO
        User user = userCommonService.getUser();
        PostAction postAction = postActionRepository.findByPostActionId(postId);
        if (postAction != null) {
            SharedPostAction sharedPostAction = new SharedPostAction();
            sharedPostAction.setPostAction(postAction);
            sharedPostAction.setUserActor(user);
            sharedPostAction.addMetadata();
            sharePostActionRepository.save(sharedPostAction);
        } else {
            throw new InvalidRequestException("Post not found!");
        }
    }

    public void deleteSharedPost(SharedPostAction sharedPostAction) {
        try {
            sharedPostAction.setDeleted(true);
            sharedPostAction.updateMetadata();
            sharePostActionRepository.save(sharedPostAction);
        } catch (Exception e) {
            throw new InvalidRequestException("Please, try again!");
        }
    }

}
