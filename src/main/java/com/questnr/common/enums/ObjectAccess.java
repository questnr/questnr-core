package com.questnr.common.enums;

public enum ObjectAccess {
  public_object(0), private_object(1);

  public Integer jsonValue;

  private ObjectAccess(Integer json) {
    this.jsonValue = json;
  }

  public Integer getJsonValue() {
    return jsonValue;
  }
}

