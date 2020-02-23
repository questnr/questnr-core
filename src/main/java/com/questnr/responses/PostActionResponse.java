package com.questnr.responses;

import com.questnr.common.enums.PublishStatus;
import com.questnr.model.entities.*;

import java.util.Date;

public class PostActionResponse {
    private Long postActionId;

    private String title;

    private String text;

    private PublishStatus status;

    private boolean featured;

    private boolean popular;

    private String videoUrl;

    private Date postDate;

    private String tags;

    private String titleTag;

    private Community community;

    private int totalLikes;

//    private int totalPostViews;

    private  int totalPostVisits;

    private int totalComments;

    public PostActionResponse(PostAction postAction){
        this.setPostActionId(postAction.getPostActionId());
        this.setTitle(postAction.getTitle());
        this.setText(postAction.getText());
        this.setStatus(postAction.getStatus());
        this.setFeatured(postAction.isFeatured());
        this.setPopular(postAction.isPopular());
        this.setPostDate(postAction.getPostDate());
        this.setVideoUrl(postAction.getVideoUrl());
        this.setTags(postAction.getTags());
        this.setTitleTag(postAction.getTitleTag());
        this.setCommunity(postAction.getCommunity());
    }

    public Long getPostActionId() {
        return postActionId;
    }

    public void setPostActionId(Long postActionId) {
        this.postActionId = postActionId;
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

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

//    public int getTotalPostViews() {
//        return totalPostViews;
//    }
//
//    public void setTotalPostViews(int totalPostViews) {
//        this.totalPostViews = totalPostViews;
//    }


    public int getTotalPostVisits() {
        return totalPostVisits;
    }

    public void setTotalPostVisits(int totalPostVisits) {
        this.totalPostVisits = totalPostVisits;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }
}
