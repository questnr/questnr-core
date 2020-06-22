package com.questnr.responses;

public class CommunityMetaProfileResponse {
    private int followers;

    private int posts;

    private int totalQuestions;

    private boolean isInTrend;

    private int trendRank;

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
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
