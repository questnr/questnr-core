package com.questnr.controllers;

import com.questnr.model.dto.TrendingPostDTO;
import com.questnr.services.TrendingPostService;
import com.questnr.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class TrendingPostController {
    @Autowired
    BaseService baseService;

    @Autowired
    TrendingPostService trendingPostService;

    @RequestMapping(value = "/trending_posts", method = RequestMethod.GET)
    List<TrendingPostDTO> getTrendingPosts(Pageable pageable) {
        return trendingPostService.getTrendingPosts(pageable);
    }
}
