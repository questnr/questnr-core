package com.questnr.model.dto;

import java.util.Date;

public class HashTagRankDTO {
    private Long hashTagId;

    private Long totalTimeBeingUsed;

    private Double rank;

    private Date date;

    public Long getHashTagId() {
        return hashTagId;
    }

    public void setHashTagId(Long hashTagId) {
        this.hashTagId = hashTagId;
    }

    public Long getTotalTimeBeingUsed() {
        return totalTimeBeingUsed;
    }

    public void setTotalTimeBeingUsed(Long totalTimeBeingUsed) {
        this.totalTimeBeingUsed = totalTimeBeingUsed;
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
