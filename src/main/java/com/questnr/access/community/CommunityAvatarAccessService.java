package com.questnr.access.community;

import com.questnr.access.user.UserCommonAccessService;
import com.questnr.model.repositories.CommunityRepository;
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
    CommunityAccessService communityAccessService;

    @Autowired
    CommunityPostActionAccessService communityPostActionAccessService;

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    UserCommonAccessService userCommonAccessService;

    @Autowired
    CommunityCommonAccessService communityCommonAccessService;

    public boolean hasAccessToCommunityAvatar(Long communityId) {
        return communityAccessService.hasAccessToCommunityBasic(communityId);
    }
}
