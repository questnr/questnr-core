package com.questnr.model.projections;

import com.questnr.model.entities.CommentAction;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "parentCommentActionProjection", types = CommentAction.class)
public interface ParentCommentProjection {
    int getCommentActionId();
}
