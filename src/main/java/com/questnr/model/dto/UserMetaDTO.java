package com.questnr.model.dto;

import com.questnr.common.enums.RelationShipType;

public class UserMetaDTO {
    private RelationShipType relationShipType = RelationShipType.none;

    public RelationShipType getRelationShipType() {
        return relationShipType;
    }

    public void setRelationShipType(RelationShipType relationShipType) {
        this.relationShipType = relationShipType;
    }
}
