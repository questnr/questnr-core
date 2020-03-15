package com.questnr.model.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.questnr.common.enums.PublishStatus;

import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.EnumBridge;
import org.springframework.stereotype.Indexed;

@Entity
@Table(name = "qr_community")
@Indexed
public class Community extends DomainObject {

    @Id
    @Column(name = "community_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "community_seq")
    @SequenceGenerator(name = "community_seq", sequenceName = "community_seq", allocationSize = 1)
    public Long id;

    @Column(name = "community_name", length = 100, unique = true)
    public String communityName;

    @Column(name = "icon_url")
    private String icon_url;

    @JsonIgnore
    @Column(name = "createdAt")
    private Date createdAt;

    @JsonIgnore
    @Column(name = "updateAt")
    private Date updateAt;

    @Column(name = "description")
    private String description;

    @Column(name = "rules")
    private String rules;

    @Column(name = "slug")
    public String slug;

    @Column(name = "ownerId")
    private long ownerId;

    @Field(bridge = @FieldBridge(impl = EnumBridge.class), store = Store.YES)
    @Enumerated(EnumType.STRING)
    @Column(name = "community_status", length = 2000)
    private PublishStatus status;

    @JsonIgnoreProperties("members")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "QR_COMMUNITY_MEMBERS",
            joinColumns = {@JoinColumn(name = "community_id", referencedColumnName = "community_id")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")})
    private Set<User> users;

    @OneToMany(mappedBy = "community")
    private Set<PostAction> postActionSet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<PostAction> getPostActionSet() {
        return postActionSet;
    }

    public void setPostActionSet(Set<PostAction> postActionSet) {
        this.postActionSet = postActionSet;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public PublishStatus getStatus() {
        return status;
    }

    public void setStatus(PublishStatus status) {
        this.status = status;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}
