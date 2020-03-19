package com.questnr.services;

import com.questnr.exceptions.InvalidInputException;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostView;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.model.repositories.PostViewRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.services.user.UserCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostViewService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostViewRepository postViewRepository;

    @Autowired
    PostActionRepository postActionRepository;

    public Page<PostView> getAllPostViewByPostId(Long postId,
                                                               Pageable pageable) {
        return postViewRepository.findByPostAction(postActionRepository.findByPostActionId(postId), pageable);
    }

    public PostView createPostView(Long postId) {
        PostView postView = new PostView();
        PostAction postAction = postActionRepository.findByPostActionId(postId);
        User user = userCommonService.getUser();
        if (postId != null) {
            if (postViewRepository.countByPostActionAndUserActor(postAction, user) == 0) {
                try {
                    postView.addMetadata();
                    postView.setUserActor(user);
                    postView.setPostAction(postAction);
                    return postViewRepository.saveAndFlush(postView);
                } catch (Exception e) {
                    LOGGER.error(PostView.class.getName() + " Exception Occurred");
                }
            }
        } else {
            throw new InvalidInputException(PostView.class.getName(), null, null);
        }
        return null;
    }

    public PostView createPostViewFromPostVisit(User user, PostAction postAction){
        PostView postView = new PostView();
        try {
            postView.setUserActor(user);
            postView.setPostAction(postAction);
            return postViewRepository.saveAndFlush(postView);
        } catch (Exception e) {
            LOGGER.error(PostView.class.getName() + " Exception Occurred");
        }
        return null;
    }
}
