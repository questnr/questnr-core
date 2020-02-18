package com.questnr.model.projections;

import com.questnr.model.entities.PostView;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "postViewProjection", types = PostView.class)
public interface PostViewProjection {
    int getPostViewId();

    UserProjection getUserActor();
}
