package com.questnr.model.dto;

import java.util.Date;

public class ChildCommentActionDTO {
    private Long commentActionId;

    private String commentObject;

    private UserOtherDTO userActorDTO;

    private Date createdAt;

    private MetaDataDTO metaData;

    private CommentActionMetaDTO commentActionMeta;

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

    public UserOtherDTO getUserActorDTO() {
        return userActorDTO;
    }

    public void setUserActorDTO(UserOtherDTO userActorDTO) {
        this.userActorDTO = userActorDTO;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public MetaDataDTO getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaDataDTO metaData) {
        this.metaData = metaData;
    }

    public CommentActionMetaDTO getCommentActionMeta() {
        return commentActionMeta;
    }

    public void setCommentActionMeta(CommentActionMetaDTO commentActionMeta) {
        this.commentActionMeta = commentActionMeta;
    }
}
