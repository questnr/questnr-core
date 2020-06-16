package com.questnr.common.enums;

public enum PostReportType {
  spam("spam"), inappropriate("inappropriate"), other("other");

  public String jsonValue;

  private PostReportType(String json) {
    this.jsonValue = json;
  }

  public String getJsonValue() {
    return jsonValue;
  }
}

