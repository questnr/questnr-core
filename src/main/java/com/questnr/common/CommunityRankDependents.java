package com.questnr.common;

public interface CommunityRankDependents {
    int PERCENT = 1;

    // @Todo: Set thresholds after some time of release.
    Double MIN_THRESHOLD = Double.valueOf(50);

    Double USER_FOLLOWERS = ((double)70)/PERCENT;
    Double POST_ACTION =  ((double)70)/PERCENT;
    Double LIKE_ACTION = ((double)70)/PERCENT;
    Double COMMENT_ACTION =  ((double)70)/PERCENT;
    Double POST_VISIT = ((double)70)/PERCENT;
    Double POST_SHARED = ((double)70)/PERCENT;
    Double PROFILE_VISIT = ((double)70)/PERCENT;

    int USER_FOLLOWER_COUNT_THRESHOLD = 2;
    int POST_COUNT_THRESHOLD = 2;
    int LIKE_COUNT_THRESHOLD = 2;
    int COMMENT_COUNT_THRESHOLD = 2;
    int POST_VISIT_COUNT_THRESHOLD = 0;
    int POST_SHARED_COUNT_THRESHOLD = 0;
    int VISIT_COUNT_THRESHOLD = -1;
}
