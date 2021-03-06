package com.questnr.model.dto.community;

import com.questnr.common.enums.CommunityPrivacy;
import com.questnr.common.enums.PublishStatus;
import com.questnr.model.dto.AvatarDTO;
import com.questnr.model.dto.MetaDataDTO;
import com.questnr.model.dto.user.UserOtherDTO;

public class CommunityDTO {

    public Long communityId;

    public String communityName;

    private String description;

    private String rules;

    public String slug;

    private UserOtherDTO ownerUserDTO;

    private PublishStatus status;

    private AvatarDTO avatarDTO;

    private int totalMembers;

    private CommunityPrivacy communityPrivacy;

    private MetaDataDTO metaData;

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

    public UserOtherDTO getOwnerUserDTO() {
        return ownerUserDTO;
    }

    public void setOwnerUserDTO(UserOtherDTO ownerUserDTO) {
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

    public int getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }

    public MetaDataDTO getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaDataDTO metaData) {
        this.metaData = metaData;
    }

    public CommunityMetaDTO getCommunityMeta() {
        return communityMeta;
    }

    public void setCommunityMeta(CommunityMetaDTO communityMeta) {
        this.communityMeta = communityMeta;
    }

    public CommunityPrivacy getCommunityPrivacy() {
        return communityPrivacy;
    }

    public void setCommunityPrivacy(CommunityPrivacy communityPrivacy) {
        this.communityPrivacy = communityPrivacy;
    }
}
