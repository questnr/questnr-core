package com.questnr.controllers;

import com.questnr.exceptions.AccessException;
import com.questnr.model.entities.CommentAction;
import com.questnr.model.projections.CommentActionProjection;
import com.questnr.requests.CommentActionRequest;
import com.questnr.services.CommentActionService;
import com.questnr.services.access.CommentActionAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class CommentActionController {

    final String errorMessage = "You don't have access to particular operation";

    @Autowired
    CommentActionService commentActionService;

    @Autowired
    CommentActionAccessService commentActionAccessService;

    @RequestMapping(value = "/posts/{postId}/comment", method = RequestMethod.GET)
    Page<CommentActionProjection> getAllCommentsByPostId(@PathVariable Long postId, Pageable pageable) {
        return commentActionService.getAllCommentActionByPostId(postId, pageable);
    }

    @RequestMapping(value = "/posts/{postId}/comment", method = RequestMethod.POST)
    CommentAction createComment(@PathVariable Long postId, @RequestBody CommentActionRequest commentActionRequest) {
        /*
         * Post Comment Security Checking
         * */
        if (commentActionAccessService.hasAccessToCommentCreation(postId)) {
            commentActionRequest.setPostId(postId);
            return commentActionService.createCommentAction(commentActionRequest);
        }
        throw new AccessException(errorMessage);
    }

    @RequestMapping(value = "/posts/{postId}/comment/{commentId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        /*
         * Post Comment Security Checking
         * */
        if (commentActionAccessService.hasAccessToCommentDeletion(postId, commentId)) {
            commentActionService.deleteCommentAction(postId, commentId);
        }else{
            throw new AccessException(errorMessage);
        }
    }
}
