package com.questnr.responses;

public class CommunityTrendRank {
    private boolean isInTrend;

    private int trendRank;

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
