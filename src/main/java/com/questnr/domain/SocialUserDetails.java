package com.questnr.domain;

import com.questnr.common.enums.LoginType;
import com.questnr.domain.facebook.FBUserDetails;
import com.questnr.domain.google.GoogleUserDetails;
import com.questnr.exceptions.AlreadyExistsException;
import com.questnr.model.entities.User;
import com.questnr.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public class SocialUserDetails {

    @Autowired
    BaseService baseService;

    public String createUsername(String fullName){
        String username = String.join(".", fullName).replaceAll("[ ](?=[ ])|[^-_A-Za-z0-9 ]+", "");
        String makingUsername = username;
        Random generateRandom = new Random();
        while(!baseService.checkIfUsernameIsTaken(makingUsername)){
            makingUsername = username + generateRandom.nextInt(1000);
        }
        return makingUsername;
    }

    public static User getUser(GoogleUserDetails googleUserDetails) {
        User user = new User();
        user.setEmailId(googleUserDetails.getEmail());
        user.setFullName(googleUserDetails.getName());
        user.setUsername(googleUserDetails.getEmail());
        user.setFirstName(googleUserDetails.getGiven_name());
        user.setLastName(googleUserDetails.getFamily_name());
        user.setEmailVerified(googleUserDetails.getEmail_verified());
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
