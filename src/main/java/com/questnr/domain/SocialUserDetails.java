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

    private String processName(String name) {
        if (name != null) {
            name = name.trim();
            return name.substring(0, Math.min(30, name.length()));
        }
        return null;
    }

    public User getUser(GoogleUserDetails googleUserDetails) {
        User user = new User();
        user.setSocialId(googleUserDetails.getSub());
        user.setEmailId(googleUserDetails.getEmail());
//        user.setFullName(googleUserDetails.getName());
        user.setUsername(baseService.createUsername(googleUserDetails.getName()));
        user.setFirstName(this.processName(googleUserDetails.getGiven_name()));
        user.setLastName(this.processName(googleUserDetails.getFamily_name()));
        user.setEmailVerified(googleUserDetails.getEmail_verified());
        user.setLoginType(LoginType.GOOGLE);
        return user;
    }

    public User getUser(FBUserDetails fbUserDetails) {
        User user = new User();
        user.setSocialId(fbUserDetails.getId());
        user.setEmailId(fbUserDetails.getEmail());
//        user.setFullName(fbUserDetails.getName());
        user.setUsername(baseService.createUsername(fbUserDetails.getName()));
        user.setFirstName(this.processName(fbUserDetails.getFirst_name()));
        user.setLastName(this.processName(fbUserDetails.getLast_name()));
        user.setEmailVerified(true);
        user.setLoginType(LoginType.FACEBOOK);
        return user;
    }
}
