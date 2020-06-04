package com.questnr.common;

public interface PostActionRankDependents {
    int PERCENT = 1;

    // @Todo: Set thresholds after some time of release.
    Double MIN_THRESHOLD = Double.valueOf(0);

    Double LIKE_ACTION = ((double)70)/PERCENT;
    Double COMMENT_ACTION =  ((double)70)/PERCENT;
    Double POST_VISIT = ((double)70)/PERCENT;
    Double POST_SHARED = ((double)70)/PERCENT;

    int LIKE_COUNT_THRESHOLD = 1;
    int COMMENT_COUNT_THRESHOLD = 1;
    int POST_VISIT_COUNT_THRESHOLD = 1;
    int POST_SHARED_COUNT_THRESHOLD = 1;
}
