package com.questnr.common.enums;

public enum NotificationType {
  post("P"),
  followed("F"),
  like("L"),
  comment("C"),
  likeComment("LC"),
  invitation("I");

  public String jsonValue;

  private NotificationType(String json) {
    this.jsonValue = json;
  }

  public String getJsonValue() {
    return jsonValue;
  }
}

