package com.questnr.model.dto.user;

public class UserWithRankDTO extends UserDTO{
    private int totalFollowers;

    private int totalPosts;

    private Double userRank;

    public int getTotalFollowers() {
        return totalFollowers;
    }

    public void setTotalFollowers(int totalFollowers) {
        this.totalFollowers = totalFollowers;
    }

    public int getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(int totalPosts) {
        this.totalPosts = totalPosts;
    }

    public Double getUserRank() {
        return userRank;
    }

    public void setUserRank(Double userRank) {
        this.userRank = userRank;
    }
}
