package com.questnr.controllers.user;

import com.questnr.requests.UserActivityRequest;
import com.questnr.responses.UserActivityResponse;
import com.questnr.services.user.UserActivityService;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class UserActivityController {
    @Autowired
    UserCommonService userCommonService;

    @Autowired
    UserActivityService userActivityService;

    @RequestMapping(value = "/activity/post", method = RequestMethod.POST)
    UserActivityResponse postSinglePage(@RequestBody UserActivityRequest userActivityRequest) {
        if (userActivityRequest.getEntityId() != null)
            return this.userActivityService.postSinglePage(userActivityRequest);
        return null;
    }

    @RequestMapping(value = "/activity/community", method = RequestMethod.POST)
    UserActivityResponse communityPage(@RequestBody UserActivityRequest userActivityRequest) {
        if (userActivityRequest.getEntityId() != null)
            return this.userActivityService.communityPage(userActivityRequest);
        return null;
    }

    @RequestMapping(value = "/activity/user", method = RequestMethod.POST)
    UserActivityResponse userPage(@RequestBody UserActivityRequest userActivityRequest) {
        if (userActivityRequest.getEntityId() != null)
            return this.userActivityService.userPage(userActivityRequest);
        return null;
    }
}
