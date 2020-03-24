package com.questnr.common.enums;

public enum PostActionPrivacy {
  PUBLIC_POST("public"), PRIVATE_POST("private");

  public String jsonValue;

  private PostActionPrivacy(String json) {
    this.jsonValue = json;
  }

  public String getJsonValue() {
    return jsonValue;
  }
}

