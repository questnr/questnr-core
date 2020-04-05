package com.questnr.model.dto;

import java.util.Date;
import java.util.Set;

public class CommentActionDTO {
    private Long commentActionId;

    private String commentObject;

    private UserDTO userActorDTO;

    private Set<ChildCommentActionDTO> childCommentDTOSet;

    private boolean childComment;

    private Date createdAt;

    public Long getCommentActionId() {
        return commentActionId;
    }

    public void setCommentActionId(Long commentActionId) {
        this.commentActionId = commentActionId;
    }

    public String getCommentObject() {
        return commentObject;
    }

    public void setCommentObject(String commentObject) {
        this.commentObject = commentObject;
    }

    public UserDTO getUserActorDTO() {
        return userActorDTO;
    }

    public void setUserActorDTO(UserDTO userActorDTO) {
        this.userActorDTO = userActorDTO;
    }

    public Set<ChildCommentActionDTO> getChildCommentDTOSet() {
        return childCommentDTOSet;
    }

    public void setChildCommentDTOSet(Set<ChildCommentActionDTO> childCommentDTOSet) {
        this.childCommentDTOSet = childCommentDTOSet;
    }

    public boolean isChildComment() {
        return childComment;
    }

    public void setChildComment(boolean childComment) {
        this.childComment = childComment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
