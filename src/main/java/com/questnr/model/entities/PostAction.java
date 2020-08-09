package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.questnr.common.NotificationTitles;
import com.questnr.common.enums.*;
import com.questnr.model.entities.media.PostMedia;
import org.hibernate.annotations.Where;
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
@Where(clause = "deleted = false")
public class PostAction extends DomainObject implements NotificationBase, PostBase {

    @Id
    @Column(name = "post_action_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_action_seq")
    @SequenceGenerator(name = "post_action_seq", sequenceName = "post_action_seq", allocationSize = 1)
    private Long postActionId;

    @Column(name = "deleted", nullable = false, columnDefinition = "bool default false")
    private boolean deleted;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
    @Column(name = "post_action_slug", unique = true, columnDefinition = "TEXT")
    private String slug;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
    @Column(name = "post_action_text", columnDefinition = "TEXT")
    private String text;

    @Field(bridge = @FieldBridge(impl = EnumBridge.class), store = Store.YES)
    @Column(name = "post_action_status")
    @Enumerated(EnumType.STRING)
    private PublishStatus status = PublishStatus.publish;

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

    @Column(name = "post_action_tags", columnDefinition = "TEXT")
    private String tags;

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

    @JsonIgnoreProperties("postAction")
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "postAction", orphanRemoval = true)
    private Set<LikeAction> likeActionSet = new HashSet<>();

    @JsonIgnoreProperties("postAction")
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "postAction", orphanRemoval = true)
    private Set<PostVisit> postVisitSet = new HashSet<>();

    @JsonIgnoreProperties("postAction")
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "postAction", orphanRemoval = true)
    private Set<CommentAction> commentActionSet = new HashSet<>();

    @JsonIgnoreProperties("postAction")
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "postAction", orphanRemoval = true)
    private Set<SharedPostAction> sharedPostActionSet = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "post_action_id", nullable = false)
    private List<PostMedia> postMediaList = new LinkedList<>();

    @JsonManagedReference(value = "meta-reference")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "postAction", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "sequence")
    private List<PostActionMetaInformation> metaList;

    @Field(bridge = @FieldBridge(impl = EnumBridge.class), store = Store.YES)
    @Column(name = "post_type")
    @Enumerated(EnumType.STRING)
    private PostType postType;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "post_poll_question_id")
    private PostPollQuestion postPollQuestion;

    @Column(name = "blog_title", columnDefinition = "TEXT", length = 200)
    private String blogTitle;

    @Field(bridge = @FieldBridge(impl = EnumBridge.class), store = Store.YES)
    @Enumerated(EnumType.STRING)
    @Column(name = "post_editor_type", nullable = false, columnDefinition = "varchar default 'normal'")
    private PostEditorType postEditorType = PostEditorType.normal;

    public Long getPostActionId() {
        return postActionId;
    }

    public void setPostActionId(Long postActionId) {
        this.postActionId = postActionId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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

    public Set<SharedPostAction> getSharedPostActionSet() {
        return sharedPostActionSet;
    }

    public void setSharedPostActionSet(Set<SharedPostAction> sharedPostActionSet) {
        this.sharedPostActionSet = sharedPostActionSet;
    }

    public List<PostMedia> getPostMediaList() {
        return postMediaList;
    }

    public void setPostMediaList(List<PostMedia> postMediaList) {
        this.postMediaList = postMediaList;
    }

    public List<PostActionMetaInformation> getMetaList() {
        return metaList;
    }

    public void setMetaList(List<PostActionMetaInformation> metaList) {
        this.metaList = metaList;
    }

    // Post Types

    public PostPollQuestion getPostPollQuestion() {
        return postPollQuestion;
    }

    public void setPostPollQuestion(PostPollQuestion postPollQuestion) {
        this.postPollQuestion = postPollQuestion;
    }

    // End Post Types

    @Override
    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public PostEditorType getPostEditorType() {
        return postEditorType;
    }

    public void setPostEditorType(PostEditorType postEditorType) {
        this.postEditorType = postEditorType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostAction that = (PostAction) o;
        return postActionId.equals(that.postActionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postActionId);
    }

    @JsonIgnore
    public NotificationType getNotificationType() {
        return NotificationType.post;
    }

    @JsonIgnore
    public String getNotificationTitles() {
        return NotificationTitles.POST_ACTION_COMMUNITY;
    }


}
