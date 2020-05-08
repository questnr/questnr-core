package com.questnr.controllers;

import com.questnr.access.SharePostActionAccessService;
import com.questnr.exceptions.AccessException;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.SharedPostAction;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.services.SharePostActionService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/user")
public class SharePostActionController {

    @Autowired
    SharePostActionAccessService sharePostActionAccessService;

    @Autowired
    SharePostActionService sharePostActionService;

    @Autowired
    final PostActionMapper postActionMapper;

    SharePostActionController() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    @RequestMapping(value = "/share/post/{postId}", method = RequestMethod.POST)
    void sharePost(@PathVariable Long postId) {
        if (sharePostActionAccessService.sharePost(postId)) {
            this.sharePostActionService.sharePost(postId);
        } else {
            throw new AccessException();
        }
    }

    @RequestMapping(value = "/share/{sharedPostId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void deleteSharedPost(@PathVariable Long sharedPostId) {
        SharedPostAction sharedPostAction = sharePostActionAccessService.deleteSharedPost(sharedPostId);
        if (sharedPostAction != null) {
            sharePostActionService.deleteSharedPost(sharedPostAction);
        } else {
            throw new AccessException();
        }
    }
}
