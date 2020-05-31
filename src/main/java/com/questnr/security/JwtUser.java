package com.questnr.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

public class JwtUser implements UserDetails {

  private final long id;
  private final String username;
  private final String firstName;
  private final String lastName;
  private final String password;
  private final String email;
  private final String fullName;
  private final String slug;
  private final Collection<? extends GrantedAuthority> authorities;
  private final Date lastPasswordResetDate;


  public JwtUser(long id, String username, String firstName, String lastName, String fullName,
      String email, String password, Collection<? extends GrantedAuthority> authorities,
      Date lastPasswordResetDate, String slug) {
    this.id = id;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
    this.lastPasswordResetDate = lastPasswordResetDate;
    this.fullName = fullName;
    this.slug = slug;
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

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
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

  public String getSlug() {
    return slug;
  }


  /**
   * @return the uniqueUserId
   */
}
