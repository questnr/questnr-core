package com.questnr.base;

import com.questnr.model.entities.PostVisit;
import com.questnr.services.PostVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class PostVisitController {

    @Autowired
    PostVisitService postVisitService;

    @RequestMapping(value = "/posts/{postId}/post_visit", method = RequestMethod.GET)
    Page<PostVisit> getAllPostVisitByPostId(@PathVariable Long postId, Pageable pageable) {
        return postVisitService.getAllPostVisitByPostId(postId, pageable);
    }

    @RequestMapping(value = "/posts/{postId}/post_visit", method = RequestMethod.POST)
    PostVisit createPostVisit(@PathVariable Long postId) {
        return postVisitService.createPostVisit(postId);
    }

}
