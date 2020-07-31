package com.questnr.common.enums;

public enum PostMediaHandlingEntityType {
    user(0),
    community(1),
    comment(2);

    public Integer jsonValue;

    private PostMediaHandlingEntityType(Integer json) {
        this.jsonValue = json;
    }

    public Integer getJsonValue() {
        return jsonValue;
    }
}
