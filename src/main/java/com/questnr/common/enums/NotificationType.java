package com.questnr.common.enums;

public enum NotificationType {
  post("P"),
  like("L"),
  comment("C"),
  likeComment("LC"),
  invitation("I"),
  followedUser("FU"),
  followedCommunity("FC");

  public String jsonValue;

  private NotificationType(String json) {
    this.jsonValue = json;
  }

  public String getJsonValue() {
    return jsonValue;
  }
}

