package com.questnr.common;


import com.questnr.model.entities.Community;

import java.util.Objects;

public class CommunitySuggestionData {

    private Community community;


    private Double similarity;

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommunitySuggestionData that = (CommunitySuggestionData) o;
        return community.equals(that.community);
    }

    @Override
    public int hashCode() {
        return Objects.hash(community);
    }
}
