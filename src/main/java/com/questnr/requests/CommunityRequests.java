package com.questnr.requests;

import com.questnr.model.entities.Community;

public class CommunityRequests extends Community {

  String communityName;
  String iconUrl;
  String descrrption;
  String rules;
  String slug;

  public String getCommunityName() {
    return communityName;
  }

  public void setCommunityName(String communityName) {
    this.communityName = communityName;
  }

  public String getIconUrl() {
    return iconUrl;
  }

  public void setIconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
  }

  public String getDescrrption() {
    return descrrption;
  }

  public void setDescrrption(String descrrption) {
    this.descrrption = descrrption;
  }

  public String getRules() {
    return rules;
  }

  public void setRules(String rules) {
    this.rules = rules;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

}
