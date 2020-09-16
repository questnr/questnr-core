package com.questnr.common.enums;

public enum SimplifiedPostType {
    post("post"),
    question("question"),
    blog("blog");

    public String jsonValue;

    private SimplifiedPostType(String json) {
        this.jsonValue = json;
    }

    public String getJsonValue() {
        return jsonValue;
    }
}
