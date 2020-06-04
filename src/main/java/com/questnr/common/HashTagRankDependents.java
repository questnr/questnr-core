package com.questnr.common;

public interface HashTagRankDependents {
    int PERCENT = 1;

    // @Todo: Set thresholds after some time of release.
    Double MIN_THRESHOLD = Double.valueOf(0);

    Double TIME_BEING_USED = ((double)70)/PERCENT;

    int TIME_BEING_USED_THRESHOLD = 1;
}
