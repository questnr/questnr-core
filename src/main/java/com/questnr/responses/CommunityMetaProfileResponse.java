package com.questnr.responses;

public class CommunityMetaProfileResponse {
    private Long followers;

    private Long posts;

    private Long totalQuestions;

    private Long totalRequests;

    private boolean isInTrend;

    private int trendRank;

    public Long getFollowers() {
        return followers;
    }

    public void setFollowers(Long followers) {
        this.followers = followers;
    }

    public Long getPosts() {
        return posts;
    }

    public void setPosts(Long posts) {
        this.posts = posts;
    }

    public Long getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Long totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Long getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(Long totalRequests) {
        this.totalRequests = totalRequests;
    }

    public boolean isInTrend() {
        return isInTrend;
    }

    public void setInTrend(boolean inTrend) {
        isInTrend = inTrend;
    }

    public int getTrendRank() {
        return trendRank;
    }

    public void setTrendRank(int trendRank) {
        this.trendRank = trendRank;
    }
}
