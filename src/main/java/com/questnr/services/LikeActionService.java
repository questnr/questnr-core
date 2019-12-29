package com.questnr.services;

import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.LikeAction;
import com.questnr.model.repositories.CommunityRepository;
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

public class LikeActionService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LikeActionRepository likeActionRepository;

    @Autowired
    PostActionRepository postActionRepository;

    public Page<LikeAction> getAllLikeActionByPostId(Long postId,
                                                     Pageable pageable) {
        return likeActionRepository.findByPostActionId(postId, pageable);
    }

    public LikeAction createLikeAction(Long postId, LikeAction likeAction){
        long userId = jwtTokenUtil.getLoggedInUserID();
        return postActionRepository.findById(postId).map(post -> {
            likeAction.setPost(post);
            likeAction.setUser(userRepository.findByUserId(userId));
            return likeActionRepository.save(likeAction);
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }

    public ResponseEntity<?> deleteLikeAction(Long postId, Long likeId){
        return likeActionRepository.findByIdAndPostId(likeId, postId).map(comment -> {
            likeActionRepository.delete(comment);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + likeId + " and postId " + postId));
    }
}
