package com.questnr.base;

import com.questnr.responses.PostActionResponse;
import com.questnr.services.TrendingPostService;
import com.questnr.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    TrendingPostService trendingPostService;

    @RequestMapping(value = "/trending_posts", method = RequestMethod.GET)
    List<PostActionResponse> getTrendingPosts(Pageable pageable) {
        return trendingPostService.getTrendingPosts(pageable);
    }
}
