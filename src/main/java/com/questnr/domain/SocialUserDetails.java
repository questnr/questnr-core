package com.questnr.domain;

import com.questnr.common.enums.LoginType;
import com.questnr.domain.facebook.FBUserDetails;
import com.questnr.domain.google.GoogleUserDetails;
import com.questnr.model.entities.User;
import com.questnr.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SocialUserDetails {

    @Autowired
    BaseService baseService;

    public User getUser(GoogleUserDetails googleUserDetails) {
        User user = new User();
        user.setEmailId(googleUserDetails.getEmail());
//        user.setFullName(googleUserDetails.getName());
        user.setUsername(baseService.createUsername(googleUserDetails.getName()));
        user.setFirstName(googleUserDetails.getGiven_name());
        user.setLastName(googleUserDetails.getFamily_name());
        user.setEmailVerified(googleUserDetails.getEmail_verified());
        user.setLoginType(LoginType.GOOGLE);
        return user;
    }

    public User getUser(FBUserDetails fbUserDetails) {
        User user = new User();
        user.setEmailId(fbUserDetails.getEmail());
//        user.setFullName(fbUserDetails.getName());
        user.setUsername(baseService.createSlug(fbUserDetails.getName()));
        user.setLoginType(LoginType.FACEBOOK);
        return user;
    }
}
