package com.questnr.requests;

public class LoginRequest {
  String emailId;
  String password;

  public String getEmailId() {
    return emailId.toLowerCase();
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
