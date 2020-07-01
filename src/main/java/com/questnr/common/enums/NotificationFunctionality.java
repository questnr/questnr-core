package com.questnr.common.enums;

public enum NotificationFunctionality {
  normal("normal"), answer("answer");

  public String jsonValue;

  private NotificationFunctionality(String json) {
    this.jsonValue = json;
  }

  public String getJsonValue() {
    return jsonValue;
  }
}

