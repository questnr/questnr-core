package com.totality.responses;

import com.totality.common.enums.PublishStatus;
import com.totality.model.entities.Post;
import com.totality.model.entities.User;
import java.util.Date;

public class CommunityResponse {

  String communityName;
  String iconUrl;
  Date createdAt;
  String descrrption;
  String rules;
  String slug;
  long ownerId ;
  long id;
  PublishStatus status;


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

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
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

  public long getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(long ownerId) {
    this.ownerId = ownerId;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public PublishStatus getStatus() {
    return status;
  }

  public void setStatus(PublishStatus status) {
    this.status = status;
  }
}
