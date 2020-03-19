package com.questnr.controllers;

import com.questnr.model.entities.PostView;
import com.questnr.model.projections.LikeActionProjection;
import com.questnr.services.PostViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class PostViewController {

    @Autowired
    PostViewService postViewService;

    @RequestMapping(value = "/posts/{postId}/post_view", method = RequestMethod.GET)
    Page<PostView> getAllViewByPostId(@PathVariable Long postId, Pageable pageable) {
        return postViewService.getAllPostViewByPostId(postId, pageable);
    }

    @RequestMapping(value = "/posts/{postId}/post_view", method = RequestMethod.POST)
    PostView createPostView(@PathVariable Long postId) {
        return postViewService.createPostView(postId);
    }

}
