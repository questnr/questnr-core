package com.questnr.model.dto;

import com.questnr.common.enums.RelationShipType;

public class CommunityMetaDTO {
    private RelationShipType relationShipType = RelationShipType.none;

    public RelationShipType getRelationShipType() {
        return relationShipType;
    }

    public void setRelationShipType(RelationShipType relationShipType) {
        this.relationShipType = relationShipType;
    }
}
