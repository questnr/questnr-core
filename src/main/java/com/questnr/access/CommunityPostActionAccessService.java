package com.questnr.access;

import com.questnr.exceptions.AccessException;
import com.questnr.exceptions.ResourceNotFoundException;
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

    public boolean isUserOwnerOfPost(User user, PostAction postAction) {
        return user.equals(postAction.getUserActor());
    }

    public boolean hasAccessToPostBaseService(Long communityId) {
        User user = userCommonService.getUser();
        Community community = communityCommonService.getCommunity(communityId);
        return communityAccessService.isUserMemberOfCommunity(user, community);
    }

    public boolean hasAccessToPostCreation(Long communityId) {
        return this.hasAccessToPostBaseService(communityId);
    }

    public PostAction hasAccessToPostModification(Long communityId, Long postId) {
        // This rights only given to user actor of the post when user is the member of the community
        PostAction postAction = postActionRepository.findByPostActionIdAndCommunity(postId, communityCommonService.getCommunity(communityId));
        if (postAction != null) {
            if (this.hasAccessToPostBaseService(communityId) && postActionAccessService.isUserOwnerOfPost(userCommonService.getUser(), postAction)) {
                return postAction;
            } else {
                throw new AccessException();
            }
        } else {
            throw new ResourceNotFoundException("Post not found!");
        }
    }

    public PostAction hasAccessToPostDeletion(Long communityId, Long postId) {
        return this.hasAccessToPostModification(communityId, postId);
    }
}
