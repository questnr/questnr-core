package com.questnr.responses;

public class LoginResponse {

  boolean loginSuccess;
  String accessToken;
  String userName;
  String errorMessage;
  boolean isFirstAttempt = true;
  boolean communitySuggestion = false;

  public LoginResponse() {

  }

  public LoginResponse(boolean loginSuccess) {
    this.loginSuccess = loginSuccess;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public boolean isLoginSuccess() {
    return loginSuccess;
  }

  public void setLoginSuccess(boolean loginSuccess) {
    this.loginSuccess = loginSuccess;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public boolean isFirstAttempt() {
    return isFirstAttempt;
  }

  public void setFirstAttempt(boolean firstAttempt) {
    isFirstAttempt = firstAttempt;
  }

  public boolean isCommunitySuggestion() {
    return communitySuggestion;
  }

  public void setCommunitySuggestion(boolean communitySuggestion) {
    this.communitySuggestion = communitySuggestion;
  }

  @Override
  public String toString() {
    return "LoginResponse [loginSuccess=" + loginSuccess + ",  accessToken=" + accessToken + "]";
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