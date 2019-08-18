package com.totality.common.enums;

public enum PublishStatus {

  draft("draft"), publish("publish"), trash("trash"), auto_draft("auto-draft");

  public String jsonValue;

  private PublishStatus(String json) {
    this.jsonValue = json;
  }

  public String getJsonValue() {
    return jsonValue;
  }
}
