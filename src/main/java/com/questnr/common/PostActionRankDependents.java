package com.questnr.common;

public interface PostActionRankDependents {
    int PERCENT = 1;

    // @Todo: Set thresholds after some time of release.
    Double MIN_THRESHOLD = Double.valueOf(0);

    Double LIKE_ACTION = ((double)30)/PERCENT;
    Double COMMENT_ACTION =  ((double)30)/PERCENT;
    Double POST_VISIT = ((double)30)/PERCENT;

    int LIKE_COUNT_THRESHOLD = 1;
    int COMMENT_COUNT_THRESHOLD = 1;
    int POST_VISIT_COUNT_THRESHOLD = 1;
}
