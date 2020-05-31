package com.questnr.model.dto;

public class LikeActionDTO {
    private Long likeActionId;

    private UserOtherDTO user;

//    private MetaDataDTO metaData;

    public Long getLikeActionId() {
        return likeActionId;
    }

    public void setLikeActionId(Long likeActionId) {
        this.likeActionId = likeActionId;
    }

    public UserOtherDTO getUser() {
        return user;
    }

    public void setUser(UserOtherDTO user) {
        this.user = user;
    }

    //    public MetaDataDTO getMetaData() {
//        return metaData;
//    }
//
//    public void setMetaData(MetaDataDTO metaData) {
//        this.metaData = metaData;
//    }
}
