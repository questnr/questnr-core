package com.totality.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.totality.common.enums.PublishStatus;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.EnumBridge;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Indexed
@Table(name = "qr_posts")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Post extends DomainObject {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_seq")
    @SequenceGenerator(name = "post_seq", sequenceName = "post_seq", allocationSize = 1)
    private Long postId;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
    @Column(name = "post_slug", length = 20000)
    private String postSlug;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
    @Column(name = "post_title", length = 10000)
    private String title;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
    @Column(name = "post_text", columnDefinition = "TEXT", length = 200000)
    private String text;

    @Field(bridge = @FieldBridge(impl = EnumBridge.class), store = Store.YES)
    @Column(name = "post_status")
    @Enumerated(EnumType.STRING)
    private PublishStatus status;

    @Column(name = "is_featured")
    private boolean featured;

    @Column(name = "is_popular")
    private boolean popular;

    @Column(name = "video_url", length = 20000)
    private String videoUrl;


    /**
     * For displaying date time on screen
     */
    @Column(name = "post_date")
    private Date postDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "post_tags")
    private String tags;

    @Column(name = "title_tag")
    private String titleTag;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    @OneToMany(mappedBy = "post")
    private List<LikeAction> likeActionList;


    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPostSlug() {
        return postSlug;
    }

    public void setPostSlug(String postSlug) {
        this.postSlug = postSlug;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public List<LikeAction> getLikeList() {
        return likeActionList;
    }

    public void setLikeList(List<LikeAction> likeList) {
        this.likeActionList = likeActionList;
    }
}
