package com.totality.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.stereotype.Indexed;


@Entity
@Table(name="qr_users")
@Indexed
public class User extends  DomainObject{

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
  @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
  private Long userId;

  @Column(name = "USERNAME", length = 50, unique = true)
  private String userName;

  @Column(name = "PASSWORD", length = 100)
  @Size(min = 4, max = 100)
  private String password;

  @Column(name = "FIRSTNAME", length = 50)
  private String firstname;

  @Column(name = "LASTNAME", length = 50)
  private String lastname;

  @Column(name = "fullname", length = 50)
  @NotNull
  @Size(min = 4, max = 100)
  private String fullName;

  @Column(name = "EMAIL", unique = true)
  @NotNull
  @Size(min = 4)
  private String emailId;

  @Column(name = "mobile", length = 15)
  private String mobileNumber;

  @Column(name = "is_mobile_verified")
  private boolean isMobileNumberVerified;

  @Column(name = "ENABLED")
  private boolean enabled;

  @JsonIgnore
  @Column(name = "LASTPASSWORDRESETDATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastPasswordResetDate;

  @JsonIgnore
  @Column(name="createdAt")
  private  Date createdAt;

  @Column(name = "avatar")
  private String avatar;

  @JsonIgnoreProperties("users")
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "QR_USER_AUTHORITY",
      joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
      inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")})
  private Set<Authority> authorities;

  public Long getUserId() {
    return userId;
  }

  public Set<Authority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<Authority> authorities) {
    this.authorities = authorities;
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

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
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
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Date getLastPasswordResetDate() {
    return lastPasswordResetDate;
  }

  public void setLastPasswordResetDate(Date lastPasswordResetDate) {
    this.lastPasswordResetDate = lastPasswordResetDate;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }
}
