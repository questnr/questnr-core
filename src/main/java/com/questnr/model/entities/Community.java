package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.questnr.common.enums.PublishStatus;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.EnumBridge;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "qr_community")
@Indexed
public class Community extends DomainObject {

    @Id
    @Column(name = "community_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "community_seq")
    @SequenceGenerator(name = "community_seq", sequenceName = "community_seq", allocationSize = 1)
    public Long communityId;

    @Column(name = "community_name", length = 100, unique = true)
    public String communityName;

    @Column(name = "description")
    private String description;

    @Column(name = "rules")
    private String rules;

    @Column(name = "slug", unique = true)
    public String slug;

    @ManyToOne
    @JoinColumn(name = "owner_user_id")
    private User ownerUser;

    @Field(bridge = @FieldBridge(impl = EnumBridge.class), store = Store.YES)
    @Enumerated(EnumType.STRING)
    @Column(name = "community_status", length = 2000)
    private PublishStatus status;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "community", orphanRemoval = true)
    private Set<CommunityUser> users = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "community", orphanRemoval = true)
    private Set<CommunityInvitedUser> invitedUsers = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "community", orphanRemoval = true)
    private Set<PostAction> postActionSet;

    @JsonManagedReference(value = "meta-reference")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "sequence")
    private List<CommunityMetaInformation> metaList;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
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

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    @JsonIgnore
    public Set<CommunityUser> getUsers() {
        return users;
    }

    public void setUsers(Set<CommunityUser> users) {
        this.users = users;
    }

    public Set<CommunityInvitedUser> getInvitedUsers() {
        return invitedUsers;
    }

    public void setInvitedUsers(Set<CommunityInvitedUser> invitedUsers) {
        this.invitedUsers = invitedUsers;
    }

    @JsonIgnore
    public Set<PostAction> getPostActionSet() {
        return postActionSet;
    }

    public void setPostActionSet(Set<PostAction> postActionSet) {
        this.postActionSet = postActionSet;
    }

    public List<CommunityMetaInformation> getMetaList() {
        return metaList;
    }

    public void setMetaList(List<CommunityMetaInformation> metaList) {
        this.metaList = metaList;
    }
}
