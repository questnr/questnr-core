package com.questnr.model.dto;

import com.questnr.common.enums.PublishStatus;

public class CommunityCardDTO {

    public Long communityId;

    public String communityName;

    private String description;

    public String slug;

    private PublishStatus status;

    private AvatarDTO avatarDTO;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PublishStatus getStatus() {
        return status;
    }

    public void setStatus(PublishStatus status) {
        this.status = status;
    }

    public AvatarDTO getAvatarDTO() {
        return avatarDTO;
    }

    public void setAvatarDTO(AvatarDTO avatarDTO) {
        this.avatarDTO = avatarDTO;
    }
}
