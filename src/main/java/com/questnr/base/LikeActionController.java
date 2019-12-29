package com.questnr.base;

import com.questnr.model.entities.LikeAction;
import com.questnr.services.BaseService;
import com.questnr.services.LikeActionService;
import com.questnr.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1")
public class LikeActionController {

    @Autowired
    LikeActionService likeActionService;

    @RequestMapping(value = "/posts/{postId}/like", method = RequestMethod.GET)
    Page<LikeAction> getAllLikesByPostId(@PathVariable(value = "postId") Long postId, Pageable pageable) {
        return likeActionService.getAllLikeActionByPostId(postId, pageable);
    }

    @RequestMapping(value = "/posts/{postId}/like", method = RequestMethod.POST)
    LikeAction createLike(@PathVariable(value = "postId") Long postId, @Valid @RequestBody LikeAction likeAction) {
        return likeActionService.createLikeAction(postId, likeAction);
    }

    @RequestMapping(value = "/posts/{postId}/like/{likeId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteLike(@PathVariable(value = "postId") Long postId,
                                        @PathVariable(value = "likeId") Long likeId) {
        return likeActionService.deleteLikeAction(postId, likeId);
    }
}
