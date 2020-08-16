package com.questnr.controllers;

import com.questnr.requests.PostVisitRequest;
import com.questnr.services.PostVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/user")
public class PostVisitController {

    @Autowired
    PostVisitService postVisitService;

    // @Todo: add public view tracking number
//    @RequestMapping(value = "/posts/visit/{postId}", method = RequestMethod.GET)
//    Page<PostVisit> getAllPostVisitByPostId(@PathVariable Long postId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
//        return postVisitService.getAllPostVisitByPostId(postId, pageable);
//    }

    @RequestMapping(value = "/posts/visit", method = RequestMethod.POST)
    void createPostVisit(@RequestBody PostVisitRequest postVisitRequest) {
        postVisitService.createPostVisit(postVisitRequest);
    }
}
