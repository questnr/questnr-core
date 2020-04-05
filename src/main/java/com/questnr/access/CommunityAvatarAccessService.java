package com.questnr.access;

import com.questnr.exceptions.AccessException;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.services.CommonService;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.user.UserCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityAvatarAccessService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    CommonService commonService;

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    PostActionAccessService postActionAccessService;


    public boolean hasAccessToCommunityAvatar(Long communityId) throws AccessException {
        return postActionAccessService.hasAccessToPostBaseService(communityId);
    }

    public boolean hasAccessToCommunityCreation(){
        return true;
    }

    public boolean hasAccessToCommunityDeletion(){
        return true;
    }
}
