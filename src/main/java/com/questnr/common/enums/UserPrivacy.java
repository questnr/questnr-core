package com.questnr.common.enums;

public enum UserPrivacy {
    pub("public"), pri("private");

    public String jsonValue;

    private UserPrivacy(String json) {
        this.jsonValue = json;
    }

    public String getJsonValue() {
        return jsonValue;
    }
}
