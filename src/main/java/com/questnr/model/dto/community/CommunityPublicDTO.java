package com.questnr.model.dto.community;

public class CommunityPublicDTO extends CommunityDTO {
    private CommunityMetaTagCardDTO metaTagCard;

    public CommunityMetaTagCardDTO getMetaTagCard() {
        return metaTagCard;
    }

    public void setMetaTagCard(CommunityMetaTagCardDTO metaTagCard) {
        this.metaTagCard = metaTagCard;
    }
}
