package com.questnr.model.projections;

import com.questnr.model.entities.LikeAction;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "likeActionProjection", types = LikeAction.class)
public interface LikeActionProjection {
    int getLikeActionId();

    UserProjection getUser();
}
