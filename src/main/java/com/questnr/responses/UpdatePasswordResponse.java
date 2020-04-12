package com.questnr.responses;

public class UpdatePasswordResponse {

  String username;
  private boolean updateSuccess;

  public UpdatePasswordResponse() {
    super();
  }

  public boolean isUpdateSuccess() {
    return updateSuccess;
  }

  public void setUpdateSuccess(boolean updateSuccess) {
    this.updateSuccess = updateSuccess;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
