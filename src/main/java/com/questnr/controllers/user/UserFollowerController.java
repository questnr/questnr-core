package com.questnr.controllers.user;

import com.questnr.access.UserFollowerAccessService;
import com.questnr.exceptions.AccessException;
import com.questnr.model.dto.UserDTO;
import com.questnr.model.mapper.CommunityMapper;
import com.questnr.model.mapper.UserMapper;
import com.questnr.services.user.UserFollowerService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class UserFollowerController {

    @Autowired
    CommunityMapper communityMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserFollowerService userFollowerService;

    @Autowired
    UserFollowerAccessService userFollowerAccessService;

    UserFollowerController() {
        userMapper = Mappers.getMapper(UserMapper.class);
        communityMapper = Mappers.getMapper(CommunityMapper.class);
    }

    // Follow user
    @RequestMapping(value = "/follow/user/{userId}", method = RequestMethod.POST)
    UserDTO followUser(@PathVariable long userId) {
        if (userFollowerAccessService.followUser(userId)) {
            return userMapper.toOthersDTO(userFollowerService.followUser(userId));
        } else {
            throw new AccessException();
        }
    }

    // Undo follow user
    @RequestMapping(value = "/follow/user/{userId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void undoFollowUser(@PathVariable long userId) {
        userFollowerService.undoFollowUser(userId);
    }
}
