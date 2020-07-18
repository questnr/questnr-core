package com.questnr.access;

import com.questnr.common.enums.CommunityPrivacy;
import com.questnr.model.entities.Community;
import com.questnr.services.community.CommunityJoinService;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityCommonAccessService {

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    CommunityJoinService communityJoinService;

    public boolean isCommunityAccessibleWithPrivacy(Community community){
        return community.getCommunityPrivacy() == CommunityPrivacy.pub ||
                (community.getCommunityPrivacy() == CommunityPrivacy.pri
                && communityJoinService.existsCommunityUser(community, userCommonService.getUser()));
    }
}
