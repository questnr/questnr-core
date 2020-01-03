package com.questnr.services;

import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.CommentAction;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.projections.CommentActionProjection;
import com.questnr.model.repositories.CommentActionRepository;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.requests.CommentActionRequest;
import com.questnr.security.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CommentActionService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentActionRepository commentActionRepository;

    @Autowired
    PostActionRepository postActionRepository;


    public Page<CommentActionProjection> getAllCommentActionByPostId(Long postId,
                                                                     Pageable pageable) {
        return commentActionRepository.findByPostAction(postActionRepository.findByPostActionId(postId), pageable);
    }

    public CommentAction createCommentAction(CommentActionRequest commentActionRequest) {
        CommentAction commentAction = new CommentAction();
        User user = userRepository.findByUserId(jwtTokenUtil.getLoggedInUserID());
        if (commentActionRequest.getParentCommentId() != null && commentActionRepository.existsByCommentActionId(commentActionRequest.getParentCommentId())) {
            CommentAction parentCommentAction = commentActionRepository.findByCommentActionId(commentActionRequest.getParentCommentId());
            commentAction.setPostAction(parentCommentAction.getPostAction());
            commentAction.setCommentObject(commentActionRequest.getCommentObject());
            commentAction.setParentCommentAction(parentCommentAction);
            commentAction.setUserActor(user);
        } else {
            commentAction.setPostAction(postActionRepository.findByPostActionId(commentActionRequest.getPostId()));
            commentAction.setCommentObject(commentActionRequest.getCommentObject());
            commentAction.setUserActor(user);
        }
        return commentActionRepository.save(commentAction);
    }

    public ResponseEntity<?> deleteCommentAction(Long postId, Long commentId) throws ResourceNotFoundException {
        long userId = jwtTokenUtil.getLoggedInUserID();
        PostAction postAction = postActionRepository.findByPostActionId(postId);
        if (postAction.getUserActor().getUserId() == userId) {
            try {
                CommentAction commentAction = commentActionRepository.findByCommentActionId(commentId);
                commentActionRepository.delete(commentAction);
                return ResponseEntity.ok().build();
            }catch (Exception e){
                throw new ResourceNotFoundException("CommentAction not found with id " + commentId);
            }
        }
        return commentActionRepository.findByPostActionAndUserActorAndCommentActionId(postAction, userRepository.findByUserId(userId), commentId).map(commentAction -> {
            commentActionRepository.delete(commentAction);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("CommentAction not found with id " + commentId));
    }
}
