package com.questnr.common.enums;

public enum CommunitySuggestionDialogActionType {
  skipped(-1), completed(1), remained(0);

  public int jsonValue;

  private CommunitySuggestionDialogActionType(int json) {
    this.jsonValue = json;
  }

  public int getJsonValue() {
    return jsonValue;
  }
}
