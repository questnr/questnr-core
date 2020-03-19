package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.questnr.common.enums.PublishStatus;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.EnumBridge;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.util.Set;

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

    @Column(name = "description")
    private String description;

    @Column(name = "rules")
    private String rules;

    @Column(name = "slug")
    public String slug;

    @ManyToOne
    @JoinColumn(name = "owner_user_id")
    private User ownerUser;

    @Field(bridge = @FieldBridge(impl = EnumBridge.class), store = Store.YES)
    @Enumerated(EnumType.STRING)
    @Column(name = "community_status", length = 2000)
    private PublishStatus status;

    @Column(name = "avatar")
    private String avatar;

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

    public User getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(User ownerUser) {
        this.ownerUser = ownerUser;
    }

    public PublishStatus getStatus() {
        return status;
    }

    public void setStatus(PublishStatus status) {
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
}
