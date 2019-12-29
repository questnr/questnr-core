package com.questnr.services;

import com.questnr.exceptions.InvalidInputException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.PostAction;
import com.questnr.model.projections.PostActionProjection;
import com.questnr.model.repositories.CommunityRepository;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class PostActionService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostActionRepository postActionRepository;

    public Page<PostActionProjection> getAllPostActionsByUserId(Pageable pageable){
        long userId = jwtTokenUtil.getLoggedInUserID();
        return postActionRepository.findAllByUser(userRepository.findByUserId(userId),pageable);
    }

    public PostAction creatPostAction(PostAction post) {
        long userId = jwtTokenUtil.getLoggedInUserID();
        if (post != null) {
            try {
                post.setUser(userRepository.findByUserId(userId));
                post.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                post.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
                post.setPostDate(Timestamp.valueOf(LocalDateTime.now()));
                return postActionRepository.saveAndFlush(post);
            } catch (Exception e) {
                LOGGER.error(PostAction.class.getName() + " Exception Occurred");
            }
        } else {
            throw new InvalidInputException(PostAction.class.getName(), null, null);
        }
        return null;
    }

    public PostAction updatePostAction(Long postId, PostAction postActionRequest){
        long userId = jwtTokenUtil.getLoggedInUserID();
        return postActionRepository.findById(postId).map(post -> {
            postActionRequest.setUser(userRepository.findByUserId(userId));
            postActionRequest.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            return postActionRepository.save(postActionRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }

    public ResponseEntity<?> deletePostAction(Long postId){
        return postActionRepository.findById(postId).map(post -> {
            postActionRepository.delete(post);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }
}
