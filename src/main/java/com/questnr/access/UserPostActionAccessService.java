package com.questnr.access;

import com.questnr.exceptions.ResourceNotFoundException;
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

@Service
public class UserPostActionAccessService {
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

    public User getAllPostsByUserId(Long userId) {
        User user = userCommonService.getUser(userId);
        if (user.equals(userCommonService.getUser()) || user.getPublic()) {
            return user;
        }
        return null;
    }

    public PostAction hasAccessToPostModification(Long postId) {
        User user = userCommonService.getUser();
        // This rights only given to user actor of the post
        PostAction postAction = postActionRepository.findByPostActionIdAndUserActor(postId, user);
        if (postAction != null) {
            return postAction;
        } else {
            throw new ResourceNotFoundException("Post not found!");
        }
    }

    public PostAction hasAccessToPostDeletion(Long postId) {
        return this.hasAccessToPostModification(postId);
    }
}
