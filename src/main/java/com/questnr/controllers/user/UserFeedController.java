package com.questnr.controllers.user;

import com.questnr.model.dto.PostActionDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.services.user.UserFeedService;
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
public class UserFeedController {
    @Autowired
    PostActionMapper postActionMapper;

    @Autowired
    UserFeedService userFeedService;

    UserFeedController() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    @RequestMapping(value = "/user/feed", method = RequestMethod.GET)
    Page<PostActionDTO> getUserFeed(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostAction> postActionDTOPage = userFeedService.getUserFeed(pageable);
        return new PageImpl<>(postActionMapper.toDTOs(postActionDTOPage.getContent()), pageable, postActionDTOPage.getTotalElements());
    }
}
