package com.questnr.services;

import com.questnr.exceptions.InvalidInputException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.HashTag;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.projections.PostActionProjection;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.model.repositories.HashTagRepository;
import com.questnr.model.repositories.PostActionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

@Service
public class PostActionService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CommonUserService commonUserService;

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    HashTagRepository hashTagRepository;

    public Page<PostAction> getAllPostActionsByUserId(Pageable pageable) {
        User user = commonUserService.getUser();
        if(user != null){
            try {
                return postActionRepository.findAllByUserActor(user, pageable);
            }catch (Exception e){
                LOGGER.error(PostAction.class.getName() + " Exception Occurred");
            }
        }else{
            throw new InvalidInputException(User.class.getName(), null, null);
        }
        return null;
    }

    public PostAction creatPostAction(PostAction post) {
        User user = commonUserService.getUser();
        if (post != null) {
            try {
                post.addMetadata();
                post.setHashTags(this.parsePostText(post.getText()));
                post.setUserActor(user);
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

    public PostAction updatePostAction(Long postId, PostAction postActionRequest) {
        User user = commonUserService.getUser();
        return postActionRepository.findById(postId).map(post -> {
            postActionRequest.setUserActor(user);
            postActionRequest.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            return postActionRepository.save(postActionRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Post not found: " + postId));
    }

    public ResponseEntity<?> deletePostAction(Long postId) {
        return postActionRepository.findById(postId).map(post -> {
            postActionRepository.delete(post);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Post  not found: " + postId));
    }

    public Set<HashTag> parsePostText(String postText) {
        User user = commonUserService.getUser();
        Set<HashTag> hashTags = new HashSet<HashTag>();
        StringTokenizer tokenizer = new StringTokenizer(postText);

        while (tokenizer.hasMoreTokens()) {
            String hashToken = tokenizer.nextToken();
            if (hashToken.startsWith("#")) {
                hashToken = hashToken.replaceAll("#", "").trim();
                if (!hashToken.equals("")) {
                    HashTag hashTag = hashTagRepository.findByHashTagValue(hashToken.toLowerCase());
                    // @Todo: Check if the hashTag exits
                    if (hashTag != null) {
                        hashTags.add(hashTag);
                    } else {
                        hashTag = new HashTag();
                        hashTag.setHashTagValue(hashToken.toLowerCase());
                        hashTag.setUserCreator(user);
                        hashTag.addMetadata();
                        hashTags.add(hashTagRepository.save(hashTag));
                    }
                }
            }
        }
        return hashTags;
    }
}
