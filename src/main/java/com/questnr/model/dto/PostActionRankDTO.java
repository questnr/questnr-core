package com.questnr.model.dto;

import java.util.Date;

public class PostActionRankDTO {
    private Long postActionId;

    private Long totalPosts;

    private Long totalLikes;

    private Long totalComments;

    private Long totalPostVisits;

    private Double rank;

    private Date date;

    public Long getPostActionId() {
        return postActionId;
    }

    public void setPostActionId(Long postActionId) {
        this.postActionId = postActionId;
    }

    public Long getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(Long totalPosts) {
        this.totalPosts = totalPosts;
    }

    public Long getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(Long totalLikes) {
        this.totalLikes = totalLikes;
    }

    public Long getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(Long totalComments) {
        this.totalComments = totalComments;
    }

    public Long getTotalPostVisits() {
        return totalPostVisits;
    }

    public void setTotalPostVisits(Long totalPostVisits) {
        this.totalPostVisits = totalPostVisits;
    }

    public Double getRank() {
        return rank;
    }

    public void setRank(Double rank) {
        this.rank = rank;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
