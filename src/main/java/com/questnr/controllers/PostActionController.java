package com.questnr.controllers;

import com.questnr.model.dto.PostActionDTO;
import com.questnr.model.dto.PostActionRequestDTO;
import com.questnr.model.dto.PostActionSharableLinkDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.services.PostActionService;
import com.questnr.services.user.UserPostActionService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
        PostAction postAction = postActionService.getPostActionFromSlug(postSlug);
        return postActionMapper.toDTO(postActionService.setPostActionMetaInformation(postAction));
    }

    // Get PostAction sharable link
    @RequestMapping(value = "/posts", method = RequestMethod.POST)
    PostActionSharableLinkDTO getPostActionSharableLink(@RequestBody Long postActionId) {
        return postActionService.getPostActionSharableLink(postActionId);
    }
}
