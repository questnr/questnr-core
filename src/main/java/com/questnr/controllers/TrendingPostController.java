package com.questnr.controllers;

import com.questnr.model.dto.TrendingPostDTO;
import com.questnr.services.BaseService;
import com.questnr.services.TrendingPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @RequestMapping(value = "/trending_posts", method = RequestMethod.GET)
    Page<TrendingPostDTO> getTrendingPostsOfTheWeek(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return trendingPostService.getTrendingPostsOfTheWeek(pageable);
    }
}
