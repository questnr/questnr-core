package com.questnr.services.access;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.services.CommonService;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.user.UserCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PostActionAccessService {
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
    CommunityAccessService communityAccessService;

    public boolean isUserOwnerOfPost(User user, PostAction postAction) {
        if (Objects.equals(user.getUserId(), postAction.getUserActor().getUserId())) {
            return true;
        }
        return false;
    }

    public boolean hasAccessToPostBaseService(Long communityId){
        User user = userCommonService.getUser();
        Community community = communityCommonService.getCommunity(communityId);
       return communityAccessService.isUserMemberOfCommunity(user, community);
    }

    public boolean hasAccessToPostCreation(Long communityId){
        return this.hasAccessToPostBaseService(communityId);
    }

    public boolean hasAccessToPostModification(Long communityId){
        return this.hasAccessToPostBaseService(communityId);
    }

    public boolean hasAccessToPostDeletion(Long communityId){
        return this.hasAccessToPostBaseService(communityId);
    }
}
