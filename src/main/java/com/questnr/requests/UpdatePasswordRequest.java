package com.questnr.requests;

public class UpdatePasswordRequest {

  // String emailId;
  String resetToken;
  String password;

  public UpdatePasswordRequest() {
    super();
  }

  public UpdatePasswordRequest(String resetToken, String password) {
    super();
    this.resetToken = resetToken;
    this.password = password;
  }

  public String getResetToken() {
    return resetToken;
  }

  public void setResetToken(String resetToken) {
    this.resetToken = resetToken;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
