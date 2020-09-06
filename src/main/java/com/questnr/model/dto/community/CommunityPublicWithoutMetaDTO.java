package com.questnr.model.dto.community;

import com.questnr.model.dto.AvatarDTO;

public class CommunityPublicWithoutMetaDTO {
    public String communityName;

    private String description;

    public String slug;

    private AvatarDTO avatarDTO;

    private CommunityMetaDTO communityMeta;

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

    public AvatarDTO getAvatarDTO() {
        return avatarDTO;
    }

    public void setAvatarDTO(AvatarDTO avatarDTO) {
        this.avatarDTO = avatarDTO;
    }

    public CommunityMetaDTO getCommunityMeta() {
        return communityMeta;
    }

    public void setCommunityMeta(CommunityMetaDTO communityMeta) {
        this.communityMeta = communityMeta;
    }
}
