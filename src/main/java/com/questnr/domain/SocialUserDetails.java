package com.questnr.domain;

import com.questnr.common.enums.LoginType;
import com.questnr.domain.facebook.FBUserDetails;
import com.questnr.domain.google.GoogleUserDetails;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.model.entities.User;
import com.questnr.services.BaseService;
import com.questnr.services.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SocialUserDetails {

    @Autowired
    BaseService baseService;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private String processName(String name) {
        if (name != null) {
            name = name.trim();
            return name.substring(0, Math.min(30, name.length()));
        }
        return null;
    }

    private String getUsernameFromEmail(String email) {
        if (email != null) {
            email = email.trim();
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
            return matcher.group(1);
        }
        return null;
    }

    public User getUser(GoogleUserDetails googleUserDetails) {
        if (googleUserDetails != null
                && googleUserDetails.getEmail() != null
                && !CommonService.isNull(googleUserDetails.getEmail())) {
            User user = new User();
            user.setSocialId(googleUserDetails.getSub());
            user.setEmailId(googleUserDetails.getEmail());
//        user.setFullName(googleUserDetails.getName());

            // Create username
            if (googleUserDetails.getName() != null)
                user.setUsername(baseService.createUsername(googleUserDetails.getName()));
            else if (googleUserDetails.getGiven_name() != null || googleUserDetails.getFamily_name() != null) {
                if (googleUserDetails.getGiven_name() != null && googleUserDetails.getFamily_name() != null) {
                    user.setUsername(baseService.createUsername(googleUserDetails.getGiven_name() + googleUserDetails.getGiven_name()));
                } else if (googleUserDetails.getGiven_name() != null && googleUserDetails.getFamily_name() == null) {
                    user.setUsername(baseService.createUsername(googleUserDetails.getGiven_name()));
                } else if (googleUserDetails.getGiven_name() == null && googleUserDetails.getFamily_name() != null) {
                    user.setUsername(baseService.createUsername(googleUserDetails.getFamily_name()));
                }
            } else
                user.setUsername(baseService.createUsername(this.getUsernameFromEmail(googleUserDetails/**/.getEmail())));

            user.setFirstName(this.processName(googleUserDetails.getGiven_name()));
            user.setLastName(this.processName(googleUserDetails.getFamily_name()));
            user.setEmailVerified(googleUserDetails.getEmail_verified());
            user.setLoginType(LoginType.GOOGLE);
            return user;
        }
        throw new InvalidRequestException("User does not have any registered email id");
    }

    public User getUser(FBUserDetails fbUserDetails) {
        if (fbUserDetails != null
                && fbUserDetails.getEmail() != null
                && !CommonService.isNull(fbUserDetails.getEmail())) {
            User user = new User();
            user.setSocialId(fbUserDetails.getId());
            user.setEmailId(fbUserDetails.getEmail());
//        user.setFullName(fbUserDetails.getName());

            // Create username
            if (fbUserDetails.getName() != null)
                user.setUsername(baseService.createUsername(fbUserDetails.getName()));
            else if (fbUserDetails.getFirst_name() != null || fbUserDetails.getLast_name() != null) {
                if (fbUserDetails.getFirst_name() != null && fbUserDetails.getLast_name() != null) {
                    user.setUsername(baseService.createUsername(fbUserDetails.getFirst_name() + fbUserDetails.getLast_name()));
                } else if (fbUserDetails.getFirst_name() != null && fbUserDetails.getLast_name() == null) {
                    user.setUsername(baseService.createUsername(fbUserDetails.getFirst_name()));
                } else if (fbUserDetails.getFirst_name() == null && fbUserDetails.getLast_name() != null) {
                    user.setUsername(baseService.createUsername(fbUserDetails.getLast_name()));
                }
            } else
                user.setUsername(baseService.createUsername(this.getUsernameFromEmail(fbUserDetails.getEmail())));

            user.setFirstName(this.processName(fbUserDetails.getFirst_name()));
            user.setLastName(this.processName(fbUserDetails.getLast_name()));
            user.setEmailVerified(true);
            user.setLoginType(LoginType.FACEBOOK);
            return user;
        }
        throw new InvalidRequestException("User does not have any registered email id");
    }
}
