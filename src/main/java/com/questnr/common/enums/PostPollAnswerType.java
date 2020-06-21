package com.questnr.common.enums;

public enum PostPollAnswerType {
    agree("P"),
    disagree("N");

    public String jsonValue;

    private PostPollAnswerType(String json) {
        this.jsonValue = json;
    }

    public String getJsonValue() {
        return jsonValue;
    }
}
