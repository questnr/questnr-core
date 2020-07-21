package com.questnr.common.enums;

public enum RelationShipType {
  none("none"),
  followed("followed"),
  requested("requested"),
  owned("owned");

  public String jsonValue;

  private RelationShipType(String json) {
    this.jsonValue = json;
  }

  public String getJsonValue() {
    return jsonValue;
  }
}

