package com.questnr.model.dto;

public class TrendingPostDTO extends PostActionDTO {
    private int totalLikes;
    private int totalPostVisits;
    private int totalComments;

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

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
