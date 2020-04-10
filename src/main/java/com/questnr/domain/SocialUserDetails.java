package com.questnr.domain;

import com.questnr.common.enums.LoginType;
import com.questnr.domain.facebook.FBUserDetails;
import com.questnr.domain.google.GoogleUserDetails;
import com.questnr.model.entities.User;

public class SocialUserDetails {

    public static User getUser(GoogleUserDetails googleUserDetails) {
        User user = new User();
        user.setEmailId(googleUserDetails.getEmail());
        user.setFullName(googleUserDetails.getName());
        user.setUsername(googleUserDetails.getEmail());
        user.setLoginType(LoginType.GOOGLE);
        return user;
    }

    public static User getUser(FBUserDetails fbUserDetails) {
        User user = new User();
        user.setEmailId(fbUserDetails.getEmail());
        user.setFullName(fbUserDetails.getName());
        user.setUsername(fbUserDetails.getEmail());
        user.setLoginType(LoginType.FACEBOOK);
        return user;
    }
}
