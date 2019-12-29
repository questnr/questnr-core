package com.questnr.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.Date;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtUser implements UserDetails {

  private final long id;
  private final String username;
  private final String firstname;
  private final String lastname;
  private final String password;
  private final String email;
  private final String fullName;
  private final String phoneNumber;
  private final Collection<? extends GrantedAuthority> authorities;
  private final Date lastPasswordResetDate;


  public JwtUser(long id, String username, String firstname, String lastname, String fullName,
      String email, String password, Collection<? extends GrantedAuthority> authorities,
      Date lastPasswordResetDate,String phoneNumber) {
    this.id = id;
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
    this.lastPasswordResetDate = lastPasswordResetDate;
    this.fullName = fullName;
    this.phoneNumber = phoneNumber;
  }

  @JsonIgnore
  public Long getId() {
    return id;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public String getEmail() {
    return email;
  }

  @JsonIgnore
  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @JsonIgnore
  public Date getLastPasswordResetDate() {
    return lastPasswordResetDate;
  }

  public String getFullName() {
    return fullName;
  }


  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * @return the uniqueUserId
   */
}
