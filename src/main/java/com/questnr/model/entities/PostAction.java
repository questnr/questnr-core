package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.questnr.common.enums.PostActionPrivacy;
import com.questnr.common.enums.PublishStatus;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;
import org.hibernate.search.bridge.builtin.EnumBridge;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.*;

@Entity
@Indexed
@Table(name = "qr_post_actions")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PostAction extends DomainObject {

    @Id
    @Column(name = "post_action_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_action_seq")
    @SequenceGenerator(name = "post_action_seq", sequenceName = "post_action_seq", allocationSize = 1)
    private Long postActionId;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
    @Column(name = "post_action_slug", length = 20000, unique = true)
    private String slug;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
    @Column(name = "post_action_title", length = 10000)
    private String title;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
    @Column(name = "post_action_text", columnDefinition = "TEXT", length = 200000)
    private String text;

    @Field(bridge = @FieldBridge(impl = EnumBridge.class), store = Store.YES)
    @Column(name = "post_action_status")
    @Enumerated(EnumType.STRING)
    private PublishStatus status;

    @Field(bridge = @FieldBridge(impl = EnumBridge.class), store = Store.YES)
    @Column(name = "post_action_privacy")
    @Enumerated(EnumType.STRING)
    private PostActionPrivacy postActionPrivacy;

    @Column(name = "is_featured")
    private boolean featured;

    @Column(name = "is_popular")
    private boolean popular;

    /**
     * For displaying date time on screen
     */
    @Column(name = "post_action_date")
    private Date postDate;

    @JsonIgnoreProperties("postActionSet")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userActor;

    @Column(name = "post_action_tags")
    private String tags;

    @Column(name = "title_tag", length = 200000)
    private String titleTag;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "qr_post_hash_tags",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "hash_tag_id")})
    private Set<HashTag> hashTags = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "postAction", orphanRemoval = true)
    private Set<LikeAction> likeActionSet = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "postAction", orphanRemoval = true)
    private Set<PostVisit> postVisitSet = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "postAction", orphanRemoval = true)
    private Set<CommentAction> commentActionSet = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_action_id", nullable = false)
    private List<PostMedia> postMediaList = new LinkedList<>();

    public Long getPostActionId() {
        return postActionId;
    }

    public void setPostActionId(Long postActionId) {
        this.postActionId = postActionId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PublishStatus getStatus() {
        return status;
    }

    public void setStatus(PublishStatus status) {
        this.status = status;
    }

    public PostActionPrivacy getPostActionPrivacy() {
        return postActionPrivacy;
    }

    public void setPostActionPrivacy(PostActionPrivacy postActionPrivacy) {
        this.postActionPrivacy = postActionPrivacy;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public boolean isPopular() {
        return popular;
    }

    public void setPopular(boolean popular) {
        this.popular = popular;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public User getUserActor() {
        return userActor;
    }

    public void setUserActor(User userActor) {
        this.userActor = userActor;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTitleTag() {
        return titleTag;
    }

    public void setTitleTag(String titleTag) {
        this.titleTag = titleTag;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public Set<LikeAction> getLikeActionSet() {
        return likeActionSet;
    }

    public void setLikeActionSet(Set<LikeAction> likeActionSet) {
        this.likeActionSet = likeActionSet;
    }

    public Set<PostVisit> getPostVisitSet() {
        return postVisitSet;
    }

    public void setPostVisitSet(Set<PostVisit> postVisitSet) {
        this.postVisitSet = postVisitSet;
    }

    public Set<CommentAction> getCommentActionSet() {
        return commentActionSet;
    }

    public void setCommentActionSet(Set<CommentAction> commentActionSet) {
        this.commentActionSet = commentActionSet;
    }

    public Set<HashTag> getHashTags() {
        return hashTags;
    }

    public void setHashTags(Set<HashTag> hashTags) {
        this.hashTags = hashTags;
    }

    public List<PostMedia> getPostMediaList() {
        return postMediaList;
    }

    public void setPostMediaList(List<PostMedia> postMediaList) {
        this.postMediaList = postMediaList;
    }
}
