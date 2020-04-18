package com.questnr.services;

import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostVisit;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.model.repositories.PostVisitRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.services.user.UserCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostVisitService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostVisitRepository postVisitRepository;

    @Autowired
    PostActionRepository postActionRepository;

    public Page<PostVisit> getAllPostVisitByPostId(Long postId,
                                                   Pageable pageable) {
        PostAction postAction = postActionRepository.findByPostActionId(postId);
        if (postAction != null)
            return postVisitRepository.findByPostAction(postAction, pageable);
        throw new ResourceNotFoundException("Post not found!");
    }

    public PostVisit createPostVisit(Long postId) {
        PostVisit postVisit = new PostVisit();
        PostAction postAction = postActionRepository.findByPostActionId(postId);
        Long userId = userCommonService.getUserId();
        User user = userRepository.findByUserId(userId);
        if (postId != null) {
            if (postVisitRepository.countByPostActionAndUserActor(postAction, user) == 0) {
                try {
//                    PostView postView = postViewService.createPostViewFromPostVisit(user, postAction);
//                    postVisit.setPostView(postView);
                    postVisit.addMetadata();
                    postVisit.setUserActor(user);
                    postVisit.setPostAction(postAction);
                    return postVisitRepository.saveAndFlush(postVisit);
                } catch (Exception e) {
                    LOGGER.error(PostVisit.class.getName() + " Exception Occurred");
                }
            }
        }
        throw new InvalidRequestException("Error occurred. Please, try again!");
    }
}
