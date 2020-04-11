package com.questnr.model.dto;

public class LikeCommentActionDTO {
    private Long likeCommentActionId;

    private UserDTO user;

    private MetaDataDTO metaData;

    public Long getLikeCommentActionId() {
        return likeCommentActionId;
    }

    public void setLikeCommentActionId(Long likeCommentActionId) {
        this.likeCommentActionId = likeCommentActionId;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public MetaDataDTO getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaDataDTO metaData) {
        this.metaData = metaData;
    }
}
