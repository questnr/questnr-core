package com.questnr.controllers;

import com.questnr.model.dto.LikeActionDTO;
import com.questnr.model.entities.LikeAction;
import com.questnr.model.mapper.LikeActionMapper;
import com.questnr.services.LikeActionService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/user")
public class LikeActionController {

    @Autowired
    LikeActionService likeActionService;

    @Autowired
    LikeActionMapper likeActionMapper;

    LikeActionController() {
        likeActionMapper = Mappers.getMapper(LikeActionMapper.class);
    }

    @RequestMapping(value = "/posts/{postId}/like", method = RequestMethod.GET)
    Page<LikeActionDTO> getAllLikesByPostId(@PathVariable Long postId, Pageable pageable) {
        Page<LikeAction> likeActionPage = likeActionService.getAllLikeActionByPostId(postId, pageable);
        return new PageImpl<>(likeActionMapper.toDTOs(likeActionPage.getContent()), pageable, likeActionPage.getTotalElements());
    }

    @RequestMapping(value = "/posts/{postId}/like", method = RequestMethod.POST)
    LikeAction createLike(@PathVariable Long postId) {
        return likeActionService.createLikeAction(postId);
    }

    @RequestMapping(value = "/posts/{postId}/like", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable Long postId) {
        likeActionService.deleteLikeAction(postId);
    }
}
