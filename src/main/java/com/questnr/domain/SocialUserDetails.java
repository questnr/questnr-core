package com.questnr.domain;

import com.questnr.common.enums.LoginType;
import com.questnr.domain.facebook.FBUserDetails;
import com.questnr.domain.google.GoogleUserDetails;
import com.questnr.model.entities.User;
import com.questnr.services.BaseService;
import com.questnr.services.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class SocialUserDetails {

    @Autowired
    BaseService baseService;

    public String createUsername(String fullName) {
        List<String> chucks = Arrays.asList(fullName.toLowerCase().split("\\s"));
        String username = CommonService.removeSpecialCharacters(String.join(".", chucks));
        String makingUsername = username;
        Random generateRandom = new Random();
        while (!baseService.checkIfUsernameIsTaken(makingUsername)) {
            makingUsername = username + (generateRandom.nextInt(1000) + 1);
        }
        return makingUsername;
    }

    public User getUser(GoogleUserDetails googleUserDetails) {
        User user = new User();
        user.setEmailId(googleUserDetails.getEmail());
        user.setFullName(googleUserDetails.getName());
        user.setUsername(this.createUsername(googleUserDetails.getName()));
        user.setFirstName(googleUserDetails.getGiven_name());
        user.setLastName(googleUserDetails.getFamily_name());
        user.setEmailVerified(googleUserDetails.getEmail_verified());
        user.setLoginType(LoginType.GOOGLE);
        return user;
    }

    public User getUser(FBUserDetails fbUserDetails) {
        User user = new User();
        user.setEmailId(fbUserDetails.getEmail());
        user.setFullName(fbUserDetails.getName());
        user.setUsername(this.createUsername(fbUserDetails.getName()));
        user.setLoginType(LoginType.FACEBOOK);
        return user;
    }
}
