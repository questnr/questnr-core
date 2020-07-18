package com.questnr.common.enums;

public enum CommunityPrivacy {
    pub("public"), pri("private");

    public String jsonValue;

    private CommunityPrivacy(String json) {
        this.jsonValue = json;
    }

    public String getJsonValue() {
        return jsonValue;
    }
}
