package com.questnr.controllers.user;

import com.questnr.responses.UserMetaProfileResponse;
import com.questnr.services.user.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserProfileController {
    @Autowired
    UserProfileService userProfileService;

    @RequestMapping(value = "/profile/meta/info", method = RequestMethod.GET)
    UserMetaProfileResponse getUserProfileDetails() {
        return this.userProfileService.getUserProfileDetails();
    }
}
