package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.stereotype.Indexed;


@Entity
@Table(name="qr_users")
@Indexed
public class User extends DomainObject{

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
  @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
  private Long userId;

  @Column(name = "username", length = 50, unique = true)
  private String userName;

  @Column(name = "password", length = 100)
  @Size(min = 4, max = 100)
  private String password;

  @Column(name = "first_name", length = 50)
  private String firstName;

  @Column(name = "last_name", length = 50)
  private String lastName;

  @Column(name = "full_name", length = 50)
  @Size(min = 4, max = 100)
  private String fullName;

  @Column(name = "email_id", unique = true)
  @NotNull
  @Size(min = 4)
  private String emailId;

  @Column(name = "mobile_number", length = 15)
  private String mobileNumber;

  @Column(name = "is_mobile_verified")
  private boolean isMobileNumberVerified;

  @Column(name = "is_enabled")
  private boolean isEnabled;

  @JsonIgnore
  @Column(name = "last_password_reset_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastPasswordResetDate;

  @JsonIgnore
  @Column(name="created_at")
  private  Date createdAt;

  @Column(name = "avatar")
  private String avatar;

  @JsonIgnoreProperties("users")
  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinTable(name = "qr_user_authority",
      joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")})
  private Set<Authority> authorities;

//  @OneToMany(mappedBy = "user")
//  private Set<PostAction> postActionSet;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public boolean isMobileNumberVerified() {
    return isMobileNumberVerified;
  }

  public void setMobileNumberVerified(boolean mobileNumberVerified) {
    isMobileNumberVerified = mobileNumberVerified;
  }

  public boolean isEnabled() {
    return isEnabled;
  }

  public void setEnabled(boolean enabled) {
    isEnabled = enabled;
  }

  public Date getLastPasswordResetDate() {
    return lastPasswordResetDate;
  }

  public void setLastPasswordResetDate(Date lastPasswordResetDate) {
    this.lastPasswordResetDate = lastPasswordResetDate;
  }

  @Override
  public Date getCreatedAt() {
    return createdAt;
  }

  @Override
  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public Set<Authority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<Authority> authorities) {
    this.authorities = authorities;
  }


//  public Set<PostAction> getPostActionSet() {
//    return postActionSet;
//  }
//
//  public void setPostActionSet(Set<PostAction> postActionSet) {
//    this.postActionSet = postActionSet;
//  }
}
