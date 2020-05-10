package com.questnr.common.enums;

public enum ResourceType {
    image("image"),
    video("video");

    public String jsonValue;

    private ResourceType(String json) {
        this.jsonValue = json;
    }

    public String getJsonValue() {
        return jsonValue;
    }
}

