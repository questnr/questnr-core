package com.questnr.common.enums;

public enum PostEditorType {
  normal("normal"),
  blog("blog");

  private String jsonValue;

  private PostEditorType(String json) {
    this.jsonValue = json;
  }

  public String getJsonValue() {
    return jsonValue;
  }
}

