package com.questnr.access;

import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityJoinAccessService {
    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    UserCommonService userCommonService;

    public boolean hasAccessToJoinCommunity(Long communityId){
        return true;
    }

    public  boolean hasAccessToInviteUser(Long communityId){
        return true;
    }
}
