package com.questnr.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TrendingPostDTO extends PostActionDTO {
    private int totalTrendingLikes;
    private int totalTrendingComments;
    private int totalTrendingPostVisits;

    @JsonIgnore
    public int getTotalTrendingLikes() {
        return totalTrendingLikes;
    }

    public void setTotalTrendingLikes(int totalTrendingLikes) {
        this.totalTrendingLikes = totalTrendingLikes;
    }

    @JsonIgnore
    public int getTotalTrendingComments() {
        return totalTrendingComments;
    }

    public void setTotalTrendingComments(int totalTrendingComments) {
        this.totalTrendingComments = totalTrendingComments;
    }

    @JsonIgnore
    public int getTotalTrendingPostVisits() {
        return totalTrendingPostVisits;
    }

    public void setTotalTrendingPostVisits(int totalTrendingPostVisits) {
        this.totalTrendingPostVisits = totalTrendingPostVisits;
    }
}
