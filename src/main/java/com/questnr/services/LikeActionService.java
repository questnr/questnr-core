package com.questnr.services;

import com.questnr.exceptions.InvalidInputException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.LikeAction;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.projections.LikeActionProjection;
import com.questnr.model.repositories.LikeActionRepository;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.security.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LikeActionService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CommonUserService commonUserService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LikeActionRepository likeActionRepository;

    @Autowired
    PostActionRepository postActionRepository;

    public Page<LikeActionProjection> getAllLikeActionByPostId(Long postId,
                                                               Pageable pageable) {
        return likeActionRepository.findByPostAction(postActionRepository.findByPostActionId(postId), pageable);
    }

    public LikeAction createLikeAction(Long postId) {
        LikeAction likeAction = new LikeAction();
        PostAction postAction = postActionRepository.findByPostActionId(postId);
        Long userId = commonUserService.getUserId();
        User user = userRepository.findByUserId(userId);
        if (postId != null) {
            if (likeActionRepository.countByPostActionAndUserActor(postAction, user) == 0) {
                try {
                    likeAction.addMetadata();
                    likeAction.setUserActor(user);
                    likeAction.setPostAction(postAction);
                    return likeActionRepository.saveAndFlush(likeAction);
                } catch (Exception e) {
                    LOGGER.error(LikeAction.class.getName() + " Exception Occurred");
                }
            }
        } else {
            throw new InvalidInputException(LikeAction.class.getName(), null, null);
        }
        return null;
    }

    public ResponseEntity<?> deleteLikeAction(Long postId) throws ResourceNotFoundException {
        Long userId = commonUserService.getUserId();
        return likeActionRepository.findByPostActionAndUserActor(postActionRepository.findByPostActionId(postId), userRepository.findByUserId(userId)).map(likeAction -> {
            likeActionRepository.delete(likeAction);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Like not found: " + postId));
    }
}
