package com.questnr.common.message.helper;

public enum MessageType {
    Error("Error"),
    Info("Info"),
    Warn("Warn");

    public String jsonValue;

    private MessageType(String json) {
        this.jsonValue = json;
    }

    public String getJsonValue() {
        return jsonValue;
    }
}
