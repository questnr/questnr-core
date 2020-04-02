package com.questnr.common.enums;

public interface UserRankDependents {
    int PERCENT = 1;
    Double USER_FOLLOWERS = ((double)70)/PERCENT;
    Double POST_ACTION =  ((double)30)/PERCENT;
    Double LIKE_ACTION = ((double)30)/PERCENT;
    Double COMMENT_ACTION =  ((double)30)/PERCENT;
    Double POST_VISIT = ((double)30)/PERCENT;
}
