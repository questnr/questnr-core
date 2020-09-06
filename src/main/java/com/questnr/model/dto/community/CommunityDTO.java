package com.questnr.model.dto.community;

import com.questnr.common.enums.CommunityPrivacy;
import com.questnr.common.enums.PublishStatus;
import com.questnr.model.dto.MetaDataDTO;
import com.questnr.model.dto.user.UserOtherDTO;

public class CommunityDTO extends CommunityPublicDTO{
    public Long communityId;

    private String rules;

    private UserOtherDTO ownerUserDTO;

    private PublishStatus status;

    private int totalMembers;

    private CommunityPrivacy communityPrivacy;

    private MetaDataDTO metaData;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
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

    public int getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }

    public CommunityPrivacy getCommunityPrivacy() {
        return communityPrivacy;
    }

    public void setCommunityPrivacy(CommunityPrivacy communityPrivacy) {
        this.communityPrivacy = communityPrivacy;
    }

    public MetaDataDTO getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaDataDTO metaData) {
        this.metaData = metaData;
    }
}
