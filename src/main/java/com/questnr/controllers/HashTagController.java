package com.questnr.controllers;

import com.questnr.model.dto.post.PostBaseDTO;
import com.questnr.model.entities.HashTag;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.model.projections.HashTagProjection;
import com.questnr.services.HashTagService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

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
    Page<HashTagProjection> searchHashTag(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam String hashTag) {
        Pageable pageable = PageRequest.of(page, size);
        return hashTagService.searchHashTag(hashTag, pageable);
    }

    @RequestMapping(value = "/trending-hash-tag-list", method = RequestMethod.GET)
    Page<HashTag> getTrendingHashTagList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        if (size > 20) size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by("hashTag.createdAt").descending()
                .and(Sort.by("trendRank")));
        return hashTagService.getTrendingHashTagList(pageable);
    }

    @RequestMapping(value = "/user/hash-tag/posts", method = RequestMethod.GET)
    Page<PostBaseDTO> getPostActionListUsingHashTag(@RequestParam() String hashTags, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return hashTagService.getPostActionListUsingHashTag(hashTags, pageable);
    }
}
