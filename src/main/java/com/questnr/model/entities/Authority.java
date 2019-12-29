package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.questnr.common.enums.AuthorityName;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "qr_authorities")
public class Authority {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authority_seq")
  @SequenceGenerator(name = "authority_seq", sequenceName = "authority_seq", allocationSize = 1)
  private Long authId;

  @Column(name = "NAME", length = 50)
  @NotNull
  @Enumerated(EnumType.STRING)
  private AuthorityName name;

  @JsonIgnoreProperties("authorities")
  @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
  private Set<User> users;

  public Long getAuthId() {
    return authId;
  }

  public void setAuthId(Long authId) {
    this.authId = authId;
  }

  public AuthorityName getName() {
    return name;
  }

  public void setName(AuthorityName name) {
    this.name = name;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Authority authority = (Authority) o;
    return name == authority.name;
  }

  @Override
  public int hashCode() {

    return Objects.hash(name);
  }
}
