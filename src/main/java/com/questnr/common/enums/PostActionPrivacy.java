package com.questnr.common.enums;

public enum PostActionPrivacy {
  public_post("public_post"), private_post("private_post");

  public String jsonValue;

  private PostActionPrivacy(String json) {
    this.jsonValue = json;
  }

  public String getJsonValue() {
    return jsonValue;
  }
}

