package com.questnr.controllers;

import com.questnr.model.dto.PostActionDTO;
import com.questnr.model.dto.PostActionSharableLinkDTO;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.services.PostActionService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(value = "/posts/{postSlug}", method = RequestMethod.GET)
    PostActionDTO getAllPostsByUserId(@PathVariable String postSlug) {
        return postActionMapper.toDTO(postActionService.setPostActionMetaInformation(postActionService.getPostActionFromSlug(postSlug)));
    }

    // Get PostAction sharable link
    @RequestMapping(value = "/posts", method = RequestMethod.POST)
    PostActionSharableLinkDTO getPostActionSharableLink(@RequestBody Long postActionId) {
        return postActionService.getPostActionSharableLink(postActionId);
    }
}
