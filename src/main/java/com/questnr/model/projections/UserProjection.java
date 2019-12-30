package com.questnr.model.projections;

import com.questnr.model.entities.Authority;
import com.questnr.model.entities.User;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;
import java.util.Set;

@Projection(name = "likeActionProjection", types = User.class)
public interface UserProjection {
    Long getUserId();

//    String getUserName();

    String getFirstname();

    String getLastname();

//    String getFullName();
//
//    String getEmailId();
//
//    String getMobileNumber();
//
//    boolean isMobileNumberVerified();
//
//    boolean isEnabled();
//
//    Date getLastPasswordResetDate();
//
//    String getAvatar();
//
//    Date getCreatedAt();
}
