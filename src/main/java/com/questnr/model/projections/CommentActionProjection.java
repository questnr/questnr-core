package com.questnr.model.projections;

import com.questnr.model.entities.CommentAction;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "commentActionProjection", types = CommentAction.class)
public interface CommentActionProjection {
    int getCommentActionId();

    String getCommentObject();

    UserProjection getUser();

    PostActionProjection getPostAction();

    ParentCommentProjection getParentCommentAction();
}
