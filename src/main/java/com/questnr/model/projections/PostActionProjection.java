package com.questnr.model.projections;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.questnr.common.enums.PublishStatus;
import com.questnr.model.entities.PostAction;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;
import java.util.Set;

@Projection(name = "postActionProjection", types = PostAction.class)
public interface PostActionProjection {

    Long getPostActionId();

    String getSlug();

    String getTitle();

    String getText();

    PublishStatus getStatus();

    Boolean isFeatured();

    Boolean isPopular();

    String getVideoUrl();

    Date getPostDate();

    String getTags();

    String getTitleTag();

    Set<HashTagProjection> getHashTags();

    UserProjection getUserActor();
}
