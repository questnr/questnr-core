package com.questnr.controllers;

import com.questnr.model.dto.PostActionDTO;
import com.questnr.model.entities.HashTag;
import com.questnr.model.entities.PostAction;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.model.projections.HashTagProjection;
import com.questnr.services.HashTagService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1")
public class HashTagController {
    @Autowired
    HashTagService hashTagService;

    @Autowired
    PostActionMapper postActionMapper;

    HashTagController() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    @RequestMapping(value = "/search/hash-tag", method = RequestMethod.GET)
    Set<HashTagProjection> searchHashTag(@RequestParam String hashTag) {
        return hashTagService.searchHashTag(hashTag);
    }

    @RequestMapping(value = "/user/trending-hash-tag-list", method = RequestMethod.GET)
    Page<HashTag> getTrendingHashTagList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        if (size > 20) size = 20;
        Pageable pageable = PageRequest.of(page, size);
        return hashTagService.getTrendingHashTagList(pageable);
    }

    @RequestMapping(value = "/user/hash-tag/{hashTagValue}/posts", method = RequestMethod.GET)
    Page<PostActionDTO> getPostActionListUsingHashTag(@PathVariable String hashTagValue, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return hashTagService.getPostActionListUsingHashTag(hashTagValue, pageable);
    }
}
