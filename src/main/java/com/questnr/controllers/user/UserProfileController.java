package com.questnr.controllers.user;

import com.questnr.access.user.UserProfileAccessService;
import com.questnr.exceptions.AccessException;
import com.questnr.model.dto.BioUserDTO;
import com.questnr.model.dto.user.UserDTO;
import com.questnr.model.entities.User;
import com.questnr.model.mapper.UserMapper;
import com.questnr.responses.UserMetaProfileResponse;
import com.questnr.services.user.UserCommonService;
import com.questnr.services.user.UserProfileService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserProfileController {

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    UserProfileAccessService userProfileAccessService;

    UserProfileController() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @RequestMapping(value = "/profile/{userSlug}", method = RequestMethod.GET)
    UserDTO getUserByUserSlug(@PathVariable String userSlug) {
        User user = userProfileAccessService.getUserByUserSlug(userSlug);
        if(user != null){
            if(user.equals(userCommonService.getUser())) {
                return userMapper.toOwnDTO(userCommonService.getUserByUserSlug(userSlug));
            }
            return userMapper.toOthersDTO(userCommonService.getUserByUserSlug(userSlug));
        }
        throw new AccessException();
    }

    @RequestMapping(value = "/profile/bio/{userSlug}", method = RequestMethod.GET)
    BioUserDTO getUserBioData(@PathVariable String userSlug) {
        User user = userProfileAccessService.getUserByUserSlug(userSlug);
        if(user != null){
            return userMapper.toBioUserDTO(user);
        }
        throw new AccessException();
    }

    @RequestMapping(value = "/profile/meta/{userSlug}/info", method = RequestMethod.GET)
    UserMetaProfileResponse getUserProfileDetails(@PathVariable String userSlug) {
        return this.userProfileService.getUserProfileDetails(userSlug);
    }

    @RequestMapping(value = "/profile/meta/{userSlug}/info/params", method = RequestMethod.GET)
    UserMetaProfileResponse getCommunityProfileDetails(@PathVariable String userSlug,
                                                            @RequestParam(defaultValue = "") String params) {
        return this.userProfileService.getUserProfileDetails(userSlug, params);
    }
}
