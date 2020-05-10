package com.questnr.controllers.user;

import com.questnr.access.UserFollowerAccessService;
import com.questnr.common.enums.RelationShipType;
import com.questnr.exceptions.AccessException;
import com.questnr.model.dto.UserDTO;
import com.questnr.model.entities.User;
import com.questnr.model.mapper.CommunityMapper;
import com.questnr.model.mapper.UserMapper;
import com.questnr.requests.UserIdRequest;
import com.questnr.services.user.UserFollowerService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/user")
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

    // Get followers of the user
    @RequestMapping(value = "/follow/following/user/{userId}", method = RequestMethod.GET)
    Page<UserDTO> getFollowersOfUser(@PathVariable Long userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        User user = userFollowerAccessService.getFollowersOfUser(userId);
        if (user != null) {
            Page<User> userPage = userFollowerService.getFollowersOfUser(user, pageable);
            return new PageImpl<>(userMapper.toOthersDTOs(userPage.getContent()), pageable, userPage.getTotalElements());
        }
        throw new AccessException();
    }

    // Get list of the users being followed by this user
    @RequestMapping(value = "/follow/user/following/{userId}", method = RequestMethod.GET)
    Page<UserDTO> getUserFollowingToOtherUsers(@PathVariable Long userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        User user = userFollowerAccessService.getUserFollowingToOtherUsers(userId);
        if (user != null) {
            Page<User> userPage = userFollowerService.getUserFollowingToOtherUsers(user, pageable);
            return new PageImpl<>(userMapper.toOthersDTOs(userPage.getContent()), pageable, userPage.getTotalElements());
        }
        throw new AccessException();
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
    void undoFollowUser(@PathVariable long userId, @RequestBody UserIdRequest userIdRequest) {
        if (userFollowerAccessService.undoFollowUser(userIdRequest.getUserId(), userId)) {
            userFollowerService.undoFollowUser(userIdRequest.getUserId(), userId);
        } else {
            throw new AccessException();
        }
    }

    // Check user follows this user
    @RequestMapping(value = "/follow/check/user/{userId}", method = RequestMethod.POST)
    RelationShipType getUserRelationShipWithUser(@PathVariable long userId) {
        return userFollowerService.getUserRelationShipWithUser(userId);
    }
}
