package com.questnr.controllers;

import com.questnr.model.dto.post.normal.PostActionForMediaDTO;
import com.questnr.model.dto.post.normal.PostActionPublicDTO;
import com.questnr.model.dto.SharableLinkDTO;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.requests.PostReportRequest;
import com.questnr.services.PostActionMetaService;
import com.questnr.services.PostActionService;
import com.questnr.services.SharableLinkService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class PostActionController {
    @Autowired
    PostActionService postActionService;

    @Autowired
    PostActionMapper postActionMapper;

    @Autowired
    SharableLinkService sharableLinkService;

    @Autowired
    PostActionMetaService postActionMetaService;

    PostActionController() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    // Get PostAction using post slug
    @RequestMapping(value = "/post/{postSlug}", method = RequestMethod.GET)
    PostActionPublicDTO getPostActionFromSlug(@PathVariable String postSlug) {
        return postActionMetaService.setPostActionMetaInformation(postActionMapper.toPublicDTO(postActionService.getPostActionFromSlug(postSlug)));
    }

    // Get PostAction sharable link
    @RequestMapping(value = "/post/{postId}/link", method = RequestMethod.POST)
    SharableLinkDTO getPostActionSharableLink(@PathVariable Long postId) {
        return sharableLinkService.getPostActionSharableLink(postId);
    }

    // Get post media links only
    @RequestMapping(value = "/user/posts/{postId}/media", method = RequestMethod.GET)
    PostActionForMediaDTO getPostActionMediaList(@PathVariable Long postId) {
        return postActionMapper.toPostActionForMediaDTO(postActionService.getPostActionMediaList(postId));
    }

    // Report post
    @RequestMapping(value = "user/posts/{postId}/report", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void reportPost(@PathVariable Long postId, @RequestBody PostReportRequest postReportRequest){
        this.postActionService.reportPost(postId, postReportRequest);
    }
}
