package com.questnr.common.enums;

public enum PostType {
    simple("S"),
    question("Q");

    public String jsonValue;

    private PostType(String json) {
        this.jsonValue = json;
    }

    public String getJsonValue() {
        return jsonValue;
    }
}
