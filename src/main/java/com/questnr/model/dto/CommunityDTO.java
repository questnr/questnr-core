package com.questnr.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.questnr.common.enums.PublishStatus;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.community.CommunityCommonService;
import org.springframework.beans.factory.annotation.Autowired;

public class CommunityDTO {

    @Autowired
    AmazonS3Client amazonS3Client;

    @Autowired
    CommunityCommonService communityCommonService;

    public Long communityId;

    public String communityName;

    private String description;

    private String rules;

    public String slug;

    private UserDTO ownerUserDTO;

    private PublishStatus status;

    private String avatar;

    private String avatarLink;

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

    public UserDTO getOwnerUserDTO() {
        return ownerUserDTO;
    }

    public void setOwnerUserDTO(UserDTO ownerUserDTO) {
        this.ownerUserDTO = ownerUserDTO;
    }

    public PublishStatus getStatus() {
        return status;
    }

    public void setStatus(PublishStatus status) {
        this.status = status;
    }

    @JsonIgnore
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarLink() {
        if(this.getAvatar() == null || this.getAvatar().trim().isEmpty()){
            return null;
        }
        return this.amazonS3Client.getS3BucketUrl(communityCommonService.joinPathToFile(this.getAvatar(),this.getCommunityId()));
    }
}
