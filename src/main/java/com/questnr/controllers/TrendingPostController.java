package com.questnr.controllers;

import com.questnr.model.dto.PostActionDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.services.BaseService;
import com.questnr.services.TrendingPostService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class TrendingPostController {
    @Autowired
    BaseService baseService;

    @Autowired
    TrendingPostService trendingPostService;

    @Autowired
    PostActionMapper postActionMapper;

    TrendingPostController() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    @RequestMapping(value = "/user/explore", method = RequestMethod.GET)
    Page<PostActionDTO> getTrendingPostsOfTheWeek(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostAction> postActionPage = trendingPostService.getTrendingPostList(pageable);
        return new PageImpl<>(postActionMapper.toDTOs(postActionPage.getContent()), pageable, postActionPage.getTotalElements());
    }
}
