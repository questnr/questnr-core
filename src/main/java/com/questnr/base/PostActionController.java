package com.questnr.base;

import com.questnr.model.entities.PostAction;
import com.questnr.services.PostActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@RestController
@RequestMapping(value = "/api/v1")
public class PostActionController {

    @Autowired
    PostActionService postActionService;

    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    Page<PostAction> getAllPostsByUserId(Pageable pageable) {
        return postActionService.getAllPostActionsByUserId(pageable);
    }

    @RequestMapping(value = "/posts", method = RequestMethod.POST)
    PostAction createPost(@RequestBody PostAction postActionRequest) {
        return postActionService.creatPostAction(postActionRequest);
    }

    @RequestMapping(value = "/posts/{postId}", method = RequestMethod.PUT)
    public PostAction updatePost(@PathVariable Long postId, @Valid @RequestBody PostAction postActionRequest) {
        return postActionService.updatePostAction(postId, postActionRequest);
    }

    @RequestMapping(value = "/posts/{postId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        return postActionService.deletePostAction(postId);
    }

}
