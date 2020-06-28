package com.questnr.model.dto.community;

import com.questnr.common.enums.PublishStatus;
import com.questnr.model.dto.AvatarDTO;

public class CommunityForPostActionDTO {

    public Long communityId;

    public String communityName;

    private String description;

    private String rules;

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

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
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
}
