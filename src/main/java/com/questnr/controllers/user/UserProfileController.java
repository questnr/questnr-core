package com.questnr.controllers.user;

import com.questnr.model.dto.UserDTO;
import com.questnr.model.mapper.UserMapper;
import com.questnr.responses.UserMetaProfileResponse;
import com.questnr.services.user.UserCommonService;
import com.questnr.services.user.UserProfileService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserProfileController {

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserCommonService userCommonService;

    UserProfileController() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @RequestMapping(value = "/profile/{userSlug}", method = RequestMethod.GET)
    UserDTO getUserByUsername(@PathVariable String userSlug) {
        return userMapper.toOthersDTO(userCommonService.getUserByUserSlug(userSlug));
    }

    @RequestMapping(value = "/profile/meta/{userSlug}/info", method = RequestMethod.GET)
    UserMetaProfileResponse getUserProfileDetails(@PathVariable String userSlug) {
        return this.userProfileService.getUserProfileDetails(userSlug);
    }
}
