package com.questnr.model.dto.post;

import com.questnr.common.enums.PostActionPrivacy;
import com.questnr.common.enums.PostActionType;
import com.questnr.common.enums.PostType;
import com.questnr.model.dto.MetaDataDTO;
import com.questnr.model.entities.HashTag;

import java.util.Set;

public class PostBaseDTO {
    private Long postActionId;
    private String slug;
    private String text;
    private PostActionPrivacy postActionPrivacy;
    private boolean featured;
    private boolean popular;
    private String tags;
    private Set<HashTag> hashTags;
    private PostActionType postActionType;
    private PostType postType;
    private MetaDataDTO metaData;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Set<HashTag> getHashTags() {
        return hashTags;
    }

    public void setHashTags(Set<HashTag> hashTags) {
        this.hashTags = hashTags;
    }

    public PostActionType getPostActionType() {
        return postActionType;
    }

    public void setPostActionType(PostActionType postActionType) {
        this.postActionType = postActionType;
    }

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public MetaDataDTO getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaDataDTO metaData) {
        this.metaData = metaData;
    }
}
