package com.questnr.access;

import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserFollowerAccessService {
    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    UserCommonService userCommonService;

    public boolean followUser(Long userId) {
        return true;
    }

    public boolean undoFollowUser(Long userId, Long userBeingFollowed) {
        if (userCommonService.getUserId().equals(userId)) {
            return true;
        } else if (userCommonService.getUserId().equals(userBeingFollowed)) {
            return true;
        }
        return false;
    }

}
