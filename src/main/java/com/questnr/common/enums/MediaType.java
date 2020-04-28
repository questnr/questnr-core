package com.questnr.common.enums;

public enum MediaType {
    image("image"),
    video("video");

    public String jsonValue;

    private MediaType(String json) {
        this.jsonValue = json;
    }

    public String getJsonValue() {
        return jsonValue;
    }
}

