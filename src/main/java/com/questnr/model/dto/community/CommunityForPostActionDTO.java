package com.questnr.model.dto.community;

import com.questnr.common.enums.PublishStatus;
import com.questnr.model.dto.AvatarDTO;
import com.questnr.model.dto.user.UserOtherDTO;

public class CommunityForPostActionDTO {

    public Long communityId;

    public String communityName;

    private String description;

    public String slug;

    private PublishStatus status;

    private AvatarDTO avatarDTO;

    private UserOtherDTO ownerUserDTO;

    private CommunityMetaDTO communityMeta;

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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

    public UserOtherDTO getOwnerUserDTO() {
        return ownerUserDTO;
    }

    public void setOwnerUserDTO(UserOtherDTO ownerUserDTO) {
        this.ownerUserDTO = ownerUserDTO;
    }

    public CommunityMetaDTO getCommunityMeta() {
        return communityMeta;
    }

    public void setCommunityMeta(CommunityMetaDTO communityMeta) {
        this.communityMeta = communityMeta;
    }
}
