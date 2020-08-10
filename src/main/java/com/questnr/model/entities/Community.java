package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.questnr.common.enums.CommunityPrivacy;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.EnumBridge;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
    private String communityName;

    @Column(name = "description", columnDefinition = "TEXT", length = 200)
    private String description;

    @Column(name = "rules")
    private String rules;

    @Column(name = "slug", unique = true)
    private String slug;

    @Column(name = "community_tags")
    private String tags;

    @Field(bridge = @FieldBridge(impl = EnumBridge.class), store = Store.YES)
    @Enumerated(EnumType.STRING)
    @Column(name = "privacy", length = 5, columnDefinition = "varchar default 'pub'")
    private CommunityPrivacy communityPrivacy;

    @ManyToOne
    @JoinColumn(name = "owner_user_id")
    private User ownerUser;

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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public CommunityPrivacy getCommunityPrivacy() {
        return communityPrivacy;
    }

    public void setCommunityPrivacy(CommunityPrivacy communityPrivacy) {
        this.communityPrivacy = communityPrivacy;
    }

    public User getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(User ownerUser) {
        this.ownerUser = ownerUser;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Community community = (Community) o;
        return communityId.equals(community.communityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(communityId);
    }
}
