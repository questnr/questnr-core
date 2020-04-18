package com.questnr.services;

import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.CommentAction;
import com.questnr.model.entities.LikeAction;
import com.questnr.model.entities.LikeCommentAction;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.CommentActionRepository;
import com.questnr.model.repositories.LikeCommentActionRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.services.user.UserCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LikeCommentActionService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LikeCommentActionRepository likeCommentActionRepository;

    @Autowired
    CommentActionRepository commentActionRepository;

    public Page<LikeCommentAction> getAllLikeActionByCommentId(Long commentId,
                                                               Pageable pageable) {
        CommentAction commentAction = commentActionRepository.findByCommentActionId(commentId);
        if (commentAction != null)
            return likeCommentActionRepository.findByCommentAction(commentAction, pageable);
        throw new ResourceNotFoundException("Comment not found");
    }

    public LikeCommentAction createLikeAction(Long commentId) {
        LikeCommentAction likeCommentAction = new LikeCommentAction();
        CommentAction commentAction = commentActionRepository.findByCommentActionId(commentId);
        Long userId = userCommonService.getUserId();
        User user = userRepository.findByUserId(userId);
        if (commentId != null) {
            if (likeCommentActionRepository.countByCommentActionAndUserActor(commentAction, user) == 0) {
                try {
                    likeCommentAction.addMetadata();
                    likeCommentAction.setUserActor(user);
                    likeCommentAction.setCommentAction(commentAction);
                    return likeCommentActionRepository.saveAndFlush(likeCommentAction);
                } catch (Exception e) {
                    LOGGER.error(LikeAction.class.getName() + " Exception Occurred");
                }
            }
        }
        throw new InvalidRequestException("Error occurred. Please, try again!");
    }

    public void deleteLikeAction(Long postId) throws ResourceNotFoundException {
        Long userId = userCommonService.getUserId();
        likeCommentActionRepository.findByCommentActionAndUserActor(commentActionRepository.findByCommentActionId(postId), userRepository.findByUserId(userId)).map(likeCommentAction -> {
            likeCommentActionRepository.delete(likeCommentAction);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Like not found"));
    }
}
