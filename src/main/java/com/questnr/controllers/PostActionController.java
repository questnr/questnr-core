package com.questnr.controllers;

import com.questnr.model.dto.PostActionDTO;
import com.questnr.model.dto.PostActionForMediaDTO;
import com.questnr.model.dto.PostActionSharableLinkDTO;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.services.PostActionService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class PostActionController {
    @Autowired
    PostActionService postActionService;

    @Autowired
    PostActionMapper postActionMapper;

    PostActionController() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    // Get PostAction using post slug
    @RequestMapping(value = "/post/{postSlug}", method = RequestMethod.GET)
    PostActionDTO getPostActionFromSlug(@PathVariable String postSlug) {
        return postActionService.setPostActionMetaInformation(postActionMapper.toDTO(postActionService.getPostActionFromSlug(postSlug)));
    }

    // Get PostAction sharable link
    @RequestMapping(value = "/post/{postId}/link", method = RequestMethod.POST)
    PostActionSharableLinkDTO getPostActionSharableLink(@PathVariable Long postId) {
        return postActionService.getPostActionSharableLink(postId);
    }

    // Get post media links only
    @RequestMapping(value = "/user/posts/{postId}/media", method = RequestMethod.GET)
    PostActionForMediaDTO getPostActionMediaList(@PathVariable Long postId) {
        return postActionMapper.toPostActionForMediaDTO(postActionService.getPostActionMediaList(postId));
    }
}
