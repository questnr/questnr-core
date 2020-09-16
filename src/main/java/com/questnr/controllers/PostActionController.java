package com.questnr.controllers;

import com.questnr.access.PostActionAccessService;
import com.questnr.common.enums.SimplifiedPostType;
import com.questnr.exceptions.AccessException;
import com.questnr.model.dto.SharableLinkDTO;
import com.questnr.model.dto.post.PostBaseDTO;
import com.questnr.model.dto.post.normal.NormalPostDTO;
import com.questnr.model.dto.post.normal.PostActionForMediaDTO;
import com.questnr.model.dto.post.question.PostPollQuestionMetaDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.mapper.CommunityMapper;
import com.questnr.model.mapper.NormalPostMapper;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.requests.PostPollAnswerRequest;
import com.questnr.requests.PostReportRequest;
import com.questnr.services.PostActionMetaService;
import com.questnr.services.PostActionService;
import com.questnr.services.PostActionSlugParsingService;
import com.questnr.services.SharableLinkService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1")
public class PostActionController {
    @Autowired
    PostActionService postActionService;

    @Autowired
    PostActionMapper postActionMapper;

    @Autowired
    SharableLinkService sharableLinkService;

    @Autowired
    PostActionMetaService postActionMetaService;

    @Autowired
    PostActionAccessService postActionAccessService;

    @Autowired
    CommunityMapper communityMapper;

    @Autowired
    PostActionSlugParsingService postActionSlugParsingService;

    PostActionController() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
        communityMapper = Mappers.getMapper(CommunityMapper.class);
    }

    // Get Post Type PostAction using post slug
    @RequestMapping(value = "/post/{postSlug}", method = RequestMethod.GET)
    PostBaseDTO getNormalTypePostActionFromSlug(@PathVariable String postSlug) {
        if (postActionAccessService.hasAccessToActionsOnPost(postSlug, SimplifiedPostType.post)) {
            return postActionSlugParsingService.getPostBase(postSlug);
        } else {
            return postActionSlugParsingService.postNotAccessible(postSlug);
        }
    }

    // Get Blog Type PostAction using post slug
    @RequestMapping(value = "/blog/{postSlug}", method = RequestMethod.GET)
    PostBaseDTO getBlogTypePostActionFromSlug(@PathVariable String postSlug) {
        if (postActionAccessService.hasAccessToActionsOnPost(postSlug, SimplifiedPostType.blog)) {
            return postActionSlugParsingService.getPostBase(postSlug);
        } else {
            return postActionSlugParsingService.postNotAccessible(postSlug);
        }
    }

    // Get Blog Type PostAction using post slug
    @RequestMapping(value = "/question/{postSlug}", method = RequestMethod.GET)
    PostBaseDTO getQuestionTypePostActionFromSlug(@PathVariable String postSlug) {
        if (postActionAccessService.hasAccessToActionsOnPost(postSlug, SimplifiedPostType.question)) {
            return postActionSlugParsingService.getPostBase(postSlug);
        } else {
            return postActionSlugParsingService.postNotAccessible(postSlug);
        }
    }

    // Get PostAction normal post data
    @RequestMapping(value = "/post/data/{postId}", method = RequestMethod.GET)
    NormalPostDTO getPostActionText(@PathVariable Long postId) {
        if (postActionAccessService.hasAccessToActionsOnPost(postId)) {
            PostAction postAction = postActionService.getPostActionById(postId);
            return NormalPostMapper.getMetaMapper(postAction, true);
        }
        throw new AccessException();
    }

    // Get PostAction sharable link
    @RequestMapping(value = "/post/{postId}/link", method = RequestMethod.POST)
    SharableLinkDTO getPostActionSharableLink(@PathVariable Long postId) {
        return sharableLinkService.getPostActionSharableLink(postId);
    }

    // Get post media links only
    @RequestMapping(value = "/user/posts/{postId}/media", method = RequestMethod.GET)
    PostActionForMediaDTO getPostActionMediaList(@PathVariable Long postId) {
        if (postActionAccessService.hasAccessToActionsOnPost(postId)) {
            return postActionMapper.toPostActionForMediaDTO(postActionService.getPostActionMediaList(postId));
        }
        throw new AccessException();
    }

    // Report post
    @RequestMapping(value = "user/posts/{postId}/report", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void reportPost(@PathVariable Long postId, @RequestBody PostReportRequest postReportRequest) {
        if (postActionAccessService.hasAccessToActionsOnPost(postId)) {
            this.postActionService.reportPost(postId, postReportRequest);
        } else {
            throw new AccessException();
        }
    }

    @RequestMapping(value = "user/posts/{postId}/poll/answer", method = RequestMethod.POST)
    PostPollQuestionMetaDTO createPollAnswerPost(@PathVariable Long postId, @Valid @RequestBody PostPollAnswerRequest postPollAnswerRequest) {
        PostAction postAction = postActionAccessService.createPollAnswerPost(postId);
        if (postAction != null) {
            return postActionService.createPollAnswerPost(postAction, postPollAnswerRequest);
        }
        throw new AccessException();
    }
}
