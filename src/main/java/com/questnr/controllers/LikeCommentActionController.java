package com.questnr.controllers;

import com.questnr.model.dto.LikeCommentActionDTO;
import com.questnr.model.entities.LikeCommentAction;
import com.questnr.model.mapper.LikeCommentActionMapper;
import com.questnr.services.LikeCommentActionService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class LikeCommentActionController {

    @Autowired
    LikeCommentActionService likeCommentActionService;

    @Autowired
    LikeCommentActionMapper likeCommentActionMapper;

    LikeCommentActionController() {
        likeCommentActionMapper = Mappers.getMapper(LikeCommentActionMapper.class);
    }

    @RequestMapping(value = "/post/comment/{commentId}/like", method = RequestMethod.GET)
    Page<LikeCommentActionDTO> getPublicLikesOnCommentByCommentId(@PathVariable Long commentId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<LikeCommentAction> likeActionPage = likeCommentActionService.getAllLikeActionByCommentId(commentId, pageable);
        return new PageImpl<>(likeCommentActionMapper.toDTOs(likeActionPage.getContent()), pageable, likeActionPage.getTotalElements());
    }

    @RequestMapping(value = "/user/posts/comment/{commentId}/like", method = RequestMethod.GET)
    Page<LikeCommentActionDTO> getAllLikesOnCommentByCommentId(@PathVariable Long commentId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<LikeCommentAction> likeActionPage = likeCommentActionService.getAllLikeActionByCommentId(commentId, pageable);
        return new PageImpl<>(likeCommentActionMapper.toDTOs(likeActionPage.getContent()), pageable, likeActionPage.getTotalElements());
    }

    @RequestMapping(value = "/user/posts/comment/{commentId}/like", method = RequestMethod.POST)
    LikeCommentAction createLikeOnComment(@PathVariable Long commentId) {
        return likeCommentActionService.createLikeAction(commentId);
    }

    @RequestMapping(value = "/user/posts/comment/{commentId}/like", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteLikeOnComment(@PathVariable Long commentId) {
        likeCommentActionService.deleteLikeAction(commentId);
    }
}
