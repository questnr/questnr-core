package com.questnr.model.dto;

public class HashTagWithRankDTO{
    private String hashTagValue;

    private Long hashTagRank;

    public String getHashTagValue() {
        return hashTagValue;
    }

    public void setHashTagValue(String hashTagValue) {
        this.hashTagValue = hashTagValue;
    }

    public Long getHashTagRank() {
        return hashTagRank;
    }

    public void setHashTagRank(Long hashTagRank) {
        this.hashTagRank = hashTagRank;
    }
}
