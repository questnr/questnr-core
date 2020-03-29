package com.questnr.model.projections;

import com.questnr.model.entities.CommentAction;
import org.springframework.data.rest.core.config.Projection;

import java.util.Set;

@Projection(name = "childCommentActionProjection", types = CommentAction.class)
public interface ChildCommentActionProjection {
    int getCommentActionId();

    String getCommentObject();
}
