package com.questnr.controllers;

import com.questnr.access.CommentActionAccessService;
import com.questnr.exceptions.AccessException;
import com.questnr.model.dto.CommentActionDTO;
import com.questnr.model.entities.CommentAction;
import com.questnr.model.mapper.CommentActionMapper;
import com.questnr.requests.CommentActionRequest;
import com.questnr.services.CommentActionService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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

    @RequestMapping(value = "/post/{postSlug}/comment", method = RequestMethod.GET)
    Page<CommentActionDTO> getPublicCommentsByPostId(@PathVariable String postSlug, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<CommentAction> commentActionPage = commentActionService.getPublicCommentsByPostId(postSlug, pageable);
        return new PageImpl<>(commentActionMapper.toDTOs(commentActionPage.getContent()), pageable, commentActionPage.getTotalElements());
    }

    @RequestMapping(value = "/user/posts/{postId}/comment", method = RequestMethod.GET)
    Page<CommentActionDTO> getAllCommentsByPostId(@PathVariable Long postId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<CommentAction> commentActionPage = commentActionService.getAllCommentActionByPostId(postId, pageable);
        return new PageImpl<>(commentActionMapper.toDTOs(commentActionPage.getContent()), pageable, commentActionPage.getTotalElements());
    }

    @RequestMapping(value = "/user/posts/{postId}/comment", method = RequestMethod.POST)
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

    @RequestMapping(value = "/user/posts/{postId}/comment/{commentId}", method = RequestMethod.DELETE)
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
