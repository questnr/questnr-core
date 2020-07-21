package com.questnr.access.community;

import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityPrivacyAccessService {

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    UserCommonService userCommonService;

    public boolean updateCommunityPrivacy(Long communityId){
        return communityCommonService.isUserOwnerOfCommunity(communityId);
    }
}
