package com.questnr.access.community;

import com.questnr.access.PostActionAccessService;
import com.questnr.model.entities.PostAction;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.services.CommonService;
import com.questnr.services.PostActionService;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.community.CommunityJoinService;
import com.questnr.services.user.UserCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityPostActionAccessService {
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

    @Autowired
    PostActionAccessService postActionAccessService;

    @Autowired
    CommunityJoinService communityJoinService;

    @Autowired
    PostActionService postActionService;

    public boolean hasAccessToPosts(Long communityId) {
        return communityAccessService.hasAccessToCommunity(communityId);
    }

    public boolean hasAccessToPostModification(Long communityId, Long postId) {
        // This rights only given to user actor of the post when user is the member of the community
        PostAction postAction = postActionRepository.findByPostActionIdAndCommunity(postId, communityCommonService.getCommunity(communityId));
        if (postActionAccessService.hasAccessToActionsOnPost(postAction)) {
            return this.hasAccessToPosts(communityId) && postActionAccessService.isUserOwnerOfPost(userCommonService.getUser(), postAction);
        }
        return false;
    }

    public boolean hasAccessToPostDeletion(Long communityId, Long postId) {
        return this.hasAccessToPostModification(communityId, postId);
    }

    public boolean hasAccessToAnswerOnPollQuestion(Long communityId, Long postId) {
        PostAction postAction = postActionRepository.findByPostActionIdAndCommunity(postId, communityCommonService.getCommunity(communityId));
        return postActionAccessService.hasAccessToActionsOnPost(postAction);
    }
}
