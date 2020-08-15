package com.questnr.responses;

import com.questnr.common.enums.CommunityPrivacy;
import com.questnr.common.enums.RelationShipType;

public class CommunityJoinResponse {
    private RelationShipType relationShipType;
    private CommunityPrivacy communityPrivacy;

    public RelationShipType getRelationShipType() {
        return relationShipType;
    }

    public void setRelationShipType(RelationShipType relationShipType) {
        this.relationShipType = relationShipType;
    }

    public CommunityPrivacy getCommunityPrivacy() {
        return communityPrivacy;
    }

    public void setCommunityPrivacy(CommunityPrivacy communityPrivacy) {
        this.communityPrivacy = communityPrivacy;
    }
}
