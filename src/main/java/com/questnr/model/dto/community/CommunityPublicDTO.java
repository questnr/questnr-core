package com.questnr.model.dto.community;

import com.questnr.model.dto.user.UserOtherDTO;

public class CommunityPublicDTO extends CommunityPublicWithoutMetaDTO{
    private UserOtherDTO ownerUserDTO;

    private CommunityMetaTagCardDTO metaTagCard;

    public UserOtherDTO getOwnerUserDTO() {
        return ownerUserDTO;
    }

    public void setOwnerUserDTO(UserOtherDTO ownerUserDTO) {
        this.ownerUserDTO = ownerUserDTO;
    }

    public CommunityMetaTagCardDTO getMetaTagCard() {
        return metaTagCard;
    }

    public void setMetaTagCard(CommunityMetaTagCardDTO metaTagCard) {
        this.metaTagCard = metaTagCard;
    }
}
