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
        User thisUser;
        try {
            thisUser = userCommonService.getUser();
        } catch (Exception e) {
            return false;
        }
        if (thisUser != null)
            return user.getUserPrivacy() == UserPrivacy.pub || user.equals(thisUser) ||
                    (user.getUserPrivacy() == UserPrivacy.pri
                            && userFollowerService.isFollowingUser(user, thisUser));
        return false;
    }
}
