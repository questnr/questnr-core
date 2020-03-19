package com.questnr.controllers;

import com.questnr.model.entities.LikeAction;
import com.questnr.model.projections.LikeActionProjection;
import com.questnr.services.LikeActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class LikeActionController {

    @Autowired
    LikeActionService likeActionService;

    @RequestMapping(value = "/posts/{postId}/like", method = RequestMethod.GET)
    Page<LikeActionProjection> getAllLikesByPostId(@PathVariable Long postId, Pageable pageable) {
        return likeActionService.getAllLikeActionByPostId(postId, pageable);
    }

    @RequestMapping(value = "/posts/{postId}/like", method = RequestMethod.POST)
    LikeAction createLike(@PathVariable Long postId) {
        return likeActionService.createLikeAction(postId);
    }

    @RequestMapping(value = "/posts/{postId}/like", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable Long postId) {
        likeActionService.deleteLikeAction(postId);
    }
}
