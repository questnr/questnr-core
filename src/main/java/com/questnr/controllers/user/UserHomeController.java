package com.questnr.controllers.user;

import com.questnr.model.dto.community.CommunityCardDTO;
import com.questnr.model.dto.post.PostBaseDTO;
import com.questnr.model.entities.Community;
import com.questnr.model.mapper.CommunityMapper;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.services.user.UserFeedService;
import com.questnr.services.user.UserHomeService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class UserHomeController {
    @Autowired
    PostActionMapper postActionMapper;

    @Autowired
    CommunityMapper communityMapper;

    @Autowired
    UserFeedService userFeedService;

    @Autowired
    UserHomeService userHomeService;

    UserHomeController() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
        communityMapper = Mappers.getMapper(CommunityMapper.class);
    }

    @RequestMapping(value = "/user/feed", method = RequestMethod.GET)
    Page<PostBaseDTO> getUserFeed(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userFeedService.getUserFeed(pageable);
    }

    // Get feed posts using string of post ids separated by coma
    @RequestMapping(value = "/user/feed/notification/posts", method = RequestMethod.GET)
    Page<PostBaseDTO> getUserFeedUsingId(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "4") int size,
                                               @RequestParam String posts) {
        Pageable pageable = PageRequest.of(page, size);
        return userFeedService.getUserFeedUsingId(posts, null, pageable);
    }

    // Get feed posts using last post id
    @RequestMapping(value = "/user/feed/notification/last/posts", method = RequestMethod.GET)
    Page<PostBaseDTO> getUserFeedUsingLastPostId(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "4") int size,
                                                 @RequestParam Long lastPostId) {
        Pageable pageable = PageRequest.of(page, size);
        return userFeedService.getUserFeedUsingId(null, lastPostId, pageable);
    }

    @RequestMapping(value = "/community/trending-community-list", method = RequestMethod.GET)
    Page<CommunityCardDTO> getTrendingCommunityList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("community.createdAt").descending()
                .and(Sort.by("trendRank")));
        Page<Community> communityPage = userHomeService.getTrendingCommunityList(pageable);
        return new PageImpl<>(communityMapper.toCommunityCards(communityPage.getContent()), pageable, communityPage.getTotalElements());
    }

    @RequestMapping(value = "/community/suggested-community-list", method = RequestMethod.GET)
    Page<CommunityCardDTO> getSuggestedCommunityList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        if (size > 20) size = 20;
        Pageable pageable = PageRequest.of(page, size);
        Page<Community> communityPage = userHomeService.getSuggestedCommunityList(pageable);
        return new PageImpl<>(communityMapper.toCommunityCards(communityPage.getContent()), pageable, communityPage.getTotalElements());
    }

    @RequestMapping(value = "/user/explore", method = RequestMethod.GET)
    Page<PostBaseDTO> getTrendingPostsOfTheWeek(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("postAction.createdAt").descending()
                .and(Sort.by("trendRank")));
        return userHomeService.getTrendingPostList(pageable);
    }

    @RequestMapping(value = "/user/explore/question", method = RequestMethod.GET)
    Page<PostBaseDTO> getTrendingPostPollQuestionsOfTheWeek(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("postAction.createdAt").descending()
                .and(Sort.by("trendRank")));
        return userHomeService.getTrendingPostPollQuestionList(pageable);
    }
}