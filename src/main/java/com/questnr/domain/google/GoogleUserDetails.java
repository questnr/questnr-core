package com.questnr.domain.google;

public class GoogleUserDetails {
    private String email;
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
    private String signUpSource;
    private Boolean email_verified;
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

    public String getGiven_name() {
        return given_name;
    }

    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public Boolean getEmail_verified() {
        return email_verified;
    }

    public void setEmail_verified(Boolean email_verified) {
        this.email_verified = email_verified;
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
