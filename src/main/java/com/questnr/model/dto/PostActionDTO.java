package com.questnr.model.dto;

import com.questnr.common.enums.PublishStatus;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.HashTag;
import com.questnr.model.entities.LikeAction;
import com.questnr.model.entities.PostVisit;

import java.util.List;
import java.util.Set;

public class PostActionDTO {
    private Long postActionId;
    private String slug;
    private String title;
    private String text;
    private PublishStatus status;
    private boolean featured;
    private boolean popular;
    private String tags;
    private String titleTag;
    private Community community;
    private Set<HashTag> hashTags;
    private Set<LikeAction> likeActionSet;
    private Set<PostVisit> postViewSet;
    private List<PostMediaDTO> postMediaDTOList;

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

    public Long getPostActionId() {
        return postActionId;
    }

    public void setPostActionId(Long postActionId) {
        this.postActionId = postActionId;
    }

    public Set<HashTag> getHashTags() {
        return hashTags;
    }

    public void setHashTags(Set<HashTag> hashTags) {
        this.hashTags = hashTags;
    }

    public Set<LikeAction> getLikeActionSet() {
        return likeActionSet;
    }

    public void setLikeActionSet(Set<LikeAction> likeActionSet) {
        this.likeActionSet = likeActionSet;
    }

    public Set<PostVisit> getPostViewSet() {
        return postViewSet;
    }

    public void setPostViewSet(Set<PostVisit> postViewSet) {
        this.postViewSet = postViewSet;
    }

    public List<PostMediaDTO> getPostMediaDTOList() {
        return postMediaDTOList;
    }

    public void setPostMediaDTOList(List<PostMediaDTO> postMediaDTOList) {
        this.postMediaDTOList = postMediaDTOList;
    }
}
