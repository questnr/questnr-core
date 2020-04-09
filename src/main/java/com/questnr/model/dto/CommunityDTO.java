package com.questnr.model.dto;

import com.questnr.common.enums.PublishStatus;
import com.questnr.model.entities.CommunityMetaInformation;

import java.util.List;

public class CommunityDTO {

    public Long communityId;

    public String communityName;

    private String description;

    private String rules;

    public String slug;

    private UserDTO ownerUserDTO;

    private PublishStatus status;

    private AvatarDTO avatarDTO;

    private List<CommunityUserForCommunityDTO> communityUsers;

    private List<CommunityMetaInformation> metaList;

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

    public AvatarDTO getAvatarDTO() {
        return avatarDTO;
    }

    public void setAvatarDTO(AvatarDTO avatarDTO) {
        this.avatarDTO = avatarDTO;
    }

    public List<CommunityUserForCommunityDTO> getCommunityUsers() {
        return communityUsers;
    }

    public void setCommunityUsers(List<CommunityUserForCommunityDTO> communityUsers) {
        this.communityUsers = communityUsers;
    }

    public List<CommunityMetaInformation> getMetaList() {
        return metaList;
    }

    public void setMetaList(List<CommunityMetaInformation> metaList) {
        this.metaList = metaList;
    }
}
