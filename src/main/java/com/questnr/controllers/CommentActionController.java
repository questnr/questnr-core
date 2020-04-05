package com.questnr.controllers;

import com.questnr.exceptions.AccessException;
import com.questnr.model.dto.CommentActionDTO;
import com.questnr.model.entities.CommentAction;
import com.questnr.model.mapper.CommentActionMapper;
import com.questnr.requests.CommentActionRequest;
import com.questnr.services.CommentActionService;
import com.questnr.access.CommentActionAccessService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    @Autowired
    CommentActionMapper commentActionMapper;

    CommentActionController() {
        commentActionMapper = Mappers.getMapper(CommentActionMapper.class);
    }

    @RequestMapping(value = "/posts/{postId}/comment", method = RequestMethod.GET)
    Page<CommentActionDTO> getAllCommentsByPostId(@PathVariable Long postId, Pageable pageable) {
        Page<CommentAction> commentActionPage = commentActionService.getAllCommentActionByPostId(postId, pageable);
        return new PageImpl<>(commentActionMapper.toDTOs(commentActionPage.getContent()), pageable, commentActionPage.getTotalElements());
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
        } else {
            throw new AccessException(errorMessage);
        }
    }
}
