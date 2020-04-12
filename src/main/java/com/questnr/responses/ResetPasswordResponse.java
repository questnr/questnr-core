package com.questnr.responses;

public class ResetPasswordResponse {

  boolean success;
  String errorMessage;

  public ResetPasswordResponse() {
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  }
