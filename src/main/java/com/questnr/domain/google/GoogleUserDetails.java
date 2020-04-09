package com.questnr.domain.google;

public class GoogleUserDetails {
    private String email;
    private String name;
    private String picture;
    private String signUpSource;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  @Override
  public String toString() {
    return "GoogleUserDetails{" +
        "email='" + email + '\'' +
        ", name='" + name + '\'' +
        ", picture='" + picture + '\'' +
        '}';
  }

  /**
   * @return the signUpSource
   */
  public String getSignUpSource() {
    return signUpSource;
  }

  /**
   * @param signUpSource the signUpSource to set
   */
  public void setSignUpSource(String signUpSource) {
    this.signUpSource = signUpSource;
  }
}
