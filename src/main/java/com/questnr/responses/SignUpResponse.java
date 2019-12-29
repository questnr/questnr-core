package com.questnr.responses;

public class SignUpResponse {

  boolean loginSucces;
  String accessToken;
  String userName;
  String errorMessage;

  public SignUpResponse() {

  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public boolean isLoginSucces() {
    return loginSucces;
  }

  public void setLoginSucces(boolean loginSucces) {
    this.loginSucces = loginSucces;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  /**
   * @return the errorMessage
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * @param errorMessage the errorMessage to set
   */
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
