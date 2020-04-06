package com.questnr.access;

import com.questnr.model.repositories.PostActionRepository;
import com.questnr.services.CommonService;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityAvatarAccessService {

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    CommonService commonService;

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    CommunityPostActionAccessService communityPostActionAccessService;


    public boolean hasAccessToCommunityAvatar(Long communityId) {
        return communityPostActionAccessService.hasAccessToPostBaseService(communityId);
    }

    public boolean hasAccessToCommunityCreation() {
        return true;
    }

    public boolean hasAccessToCommunityDeletion() {
        return true;
    }

    public boolean hasAccessToGetCommunityUsers() {
        return true;
    }
}
