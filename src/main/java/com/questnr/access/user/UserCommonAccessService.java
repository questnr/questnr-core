package com.questnr.access.user;

import com.questnr.common.enums.UserPrivacy;
import com.questnr.model.entities.User;
import com.questnr.services.user.UserCommonService;
import com.questnr.services.user.UserFollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCommonAccessService {

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    UserFollowerService userFollowerService;

    public boolean isUserAccessibleWithPrivacy(User user) {
        return user.equals(userCommonService.getUser()) ||
                user.getUserPrivacy() == UserPrivacy.pub ||
                (user.getUserPrivacy() == UserPrivacy.pri
                        && userFollowerService.isFollowingUser(user, userCommonService.getUser()));
    }
}
