package com.questnr.common.enums;

public enum PostActionType {
  normal("normal"), shared("shared");

  public String jsonValue;

  private PostActionType(String json) {
    this.jsonValue = json;
  }

  public String getJsonValue() {
    return jsonValue;
  }
}

