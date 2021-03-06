package com.questnr.controllers;

import com.questnr.access.LikeActionAccessService;
import com.questnr.exceptions.AccessException;
import com.questnr.model.dto.LikeActionDTO;
import com.questnr.model.dto.user.UserOtherDTO;
import com.questnr.model.entities.LikeAction;
import com.questnr.model.entities.User;
import com.questnr.model.mapper.LikeActionMapper;
import com.questnr.model.mapper.UserMapper;
import com.questnr.services.LikeActionService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class LikeActionController {

    @Autowired
    LikeActionService likeActionService;

    @Autowired
    LikeActionMapper likeActionMapper;

    @Autowired
    LikeActionAccessService likeActionAccessService;

    @Autowired
    UserMapper userMapper;

    LikeActionController() {
        likeActionMapper = Mappers.getMapper(LikeActionMapper.class);
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @RequestMapping(value = "/post/{postSlug}/like", method = RequestMethod.GET)
    Page<LikeActionDTO> getPublicLikesByPostId(@PathVariable String postSlug, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
        if (likeActionAccessService.hasAccessToPostLikeAction(postSlug)) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<LikeAction> likeActionPage = likeActionService.getPublicLikesByPostId(postSlug, pageable);
            return new PageImpl<>(likeActionMapper.toDTOs(likeActionPage.getContent()), pageable, likeActionPage.getTotalElements());
        }
        throw new AccessException();
    }

    @RequestMapping(value = "/user/posts/{postId}/like", method = RequestMethod.GET)
    Page<LikeActionDTO> getAllLikesByPostId(@PathVariable Long postId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
        if (likeActionAccessService.hasAccessToPostLikeAction(postId)) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<LikeAction> likeActionPage = likeActionService.getAllLikeActionByPostId(postId, pageable);
            return new PageImpl<>(likeActionMapper.toDTOs(likeActionPage.getContent()), pageable, likeActionPage.getTotalElements());
        }
        throw new AccessException();
    }

    @RequestMapping(value = "/user/posts/{postId}/like", method = RequestMethod.POST)
    LikeActionDTO createLike(@PathVariable Long postId) {
        if (likeActionAccessService.hasAccessToPostLikeAction(postId)) {
            return likeActionMapper.toDTO(likeActionService.createLikeAction(postId));
        }
        throw new AccessException();
    }

    @RequestMapping(value = "/user/posts/{postId}/like", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable Long postId) {
        if (likeActionAccessService.hasAccessToPostLikeAction(postId)) {
            likeActionService.deleteLikeAction(postId);
        }
        else{
            throw new AccessException();
        }
    }

    @RequestMapping(value = "/user/posts/{postId}/like/search/user", method = RequestMethod.GET)
    Page<UserOtherDTO> searchUserOnLikeListOfPost(@PathVariable Long postId, @RequestParam String userString, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
        if (likeActionAccessService.hasAccessToPostLikeAction(postId)) {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> userPage = likeActionService.searchUserOnLikeListOfPost(postId, userString, pageable);
            return new PageImpl<>(userMapper.toOthersDTOs(userPage.getContent()), pageable, userPage.getTotalElements());
        }
        throw new AccessException();
    }
}
