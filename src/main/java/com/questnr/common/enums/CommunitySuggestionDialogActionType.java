package com.questnr.common.enums;

public enum CommunitySuggestionDialogActionType {
  skipped("skipped"), completed("completed"), remained("remained");

  public String jsonValue;

  private CommunitySuggestionDialogActionType(String json) {
    this.jsonValue = json;
  }

  public String getJsonValue() {
    return jsonValue;
  }
}
