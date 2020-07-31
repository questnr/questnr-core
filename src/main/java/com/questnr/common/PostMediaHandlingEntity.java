package com.questnr.common;

import com.questnr.common.enums.PostMediaHandlingEntityType;

public class PostMediaHandlingEntity {
    private PostMediaHandlingEntityType postMediaHandlingEntityType;
    private Long entityId;

    public PostMediaHandlingEntity(){
        this.postMediaHandlingEntityType = PostMediaHandlingEntityType.user;
    }

    public PostMediaHandlingEntity(PostMediaHandlingEntityType postMediaHandlingEntityType, Long entityId) {
        this.postMediaHandlingEntityType = PostMediaHandlingEntityType.community;
        this.entityId = entityId;
    }

    public PostMediaHandlingEntityType getPostMediaHandlingEntityType() {
        return postMediaHandlingEntityType;
    }

    public void setPostMediaHandlingEntityType(PostMediaHandlingEntityType postMediaHandlingEntityType) {
        this.postMediaHandlingEntityType = postMediaHandlingEntityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public boolean isBelongUser(){
        return this.postMediaHandlingEntityType == PostMediaHandlingEntityType.user;
    }

    public boolean isBelongCommunity(){
        return this.postMediaHandlingEntityType == PostMediaHandlingEntityType.community;
    }

    public boolean isBelongComment(){
        return this.postMediaHandlingEntityType == PostMediaHandlingEntityType.comment;
    }
}