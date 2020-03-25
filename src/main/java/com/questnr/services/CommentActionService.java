package com.questnr.services;

import com.questnr.exceptions.InvalidInputException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.CommentAction;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.projections.CommentActionProjection;
import com.questnr.model.repositories.CommentActionRepository;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.requests.CommentActionRequest;
import com.questnr.services.user.UserCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentActionService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentActionRepository commentActionRepository;

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    CommonService commonService;

    public Page<CommentActionProjection> getAllCommentActionByPostId(Long postId,
                                                                     Pageable pageable) {
        return commentActionRepository.findByPostAction(postActionRepository.findByPostActionId(postId), pageable);
    }

    public CommentAction createCommentAction(CommentActionRequest commentActionRequest) {
        CommentAction commentAction = new CommentAction();
        User user = userCommonService.getUser();
        PostAction postAction;
        if (commentActionRequest != null) {
            try {
                if (commentActionRequest.getParentCommentId() != null && commentActionRepository.existsByCommentActionId(commentActionRequest.getParentCommentId())) {
                    CommentAction parentCommentAction = commentActionRepository.findByCommentActionId(commentActionRequest.getParentCommentId());
                    commentAction.setParentCommentAction(parentCommentAction);
                    postAction = parentCommentAction.getPostAction();
                } else {
                    postAction = postActionRepository.findByPostActionId(commentActionRequest.getPostId());
                }
                commentAction.setPostAction(postAction);
                commentAction.setCommentObject(commentActionRequest.getCommentObject());
                commentAction.addMetadata();
                commentAction.setUserActor(user);
                return commentActionRepository.save(commentAction);
            } catch (Exception e) {
                LOGGER.error(CommentAction.class.getName() + " Exception Occurred");
            }
        } else {
            throw new InvalidInputException(CommentAction.class.getName(), null, null);
        }
        return null;
    }

    public void deleteCommentAction(Long postId, Long commentId) throws ResourceNotFoundException {
        Long userId = userCommonService.getUserId();
        PostAction postAction = postActionRepository.findByPostActionId(postId);
        CommentAction commentAction = this.getCommentActionUsingPostActionAndUserIdAndCommentId(postAction, userId, commentId);
        if (commentAction != null) {
            commentActionRepository.delete(commentAction);
        } else {
            throw new ResourceNotFoundException("Comment not found!");
        }
    }

    // If user has made the comment, then he and the post owner can only take action on the same comment
    public CommentAction getCommentActionUsingPostActionAndUserIdAndCommentId(PostAction postAction, Long userId, Long commentId) {
        try {
            if (postAction.getUserActor().getUserId() == userId) {
                return commentActionRepository.findByCommentActionId(commentId);
            } else {
                return commentActionRepository.findByPostActionAndUserActorAndCommentActionId(postAction, userRepository.findByUserId(userId), commentId);
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("Comment not found!");
        }
    }
}
