package com.questnr.model.dto;

import java.util.Date;

public class CommunityRankDTO {

    private Long communityId;

    private Long totalFollowers;

    private Long totalPosts;

    private Long totalLikes;

    private Long totalComments;

    private Long totalVisits;

    private Long totalPostVisits;

    private Long totalPostShared;

    private Double rank;

    private Date date;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getTotalFollowers() {
        return totalFollowers;
    }

    public void setTotalFollowers(Long totalFollowers) {
        this.totalFollowers = totalFollowers;
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

    public Long getTotalVisits() {
        return totalVisits;
    }

    public void setTotalVisits(Long totalVisits) {
        this.totalVisits = totalVisits;
    }

    public Long getTotalPostVisits() {
        return totalPostVisits;
    }

    public void setTotalPostVisits(Long totalPostVisits) {
        this.totalPostVisits = totalPostVisits;
    }

    public Long getTotalPostShared() {
        return totalPostShared;
    }

    public void setTotalPostShared(Long totalPostShared) {
        this.totalPostShared = totalPostShared;
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
