package com.questnr.model.projections;

import com.questnr.model.entities.User;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "likeActionProjection", types = User.class)
public interface UserProjection {
    Long getUserId();

    String getUserName();

    String getFirstName();

    String getLastName();

    String getFullName();
}
