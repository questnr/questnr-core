package com.questnr.model.mapper;

import com.questnr.model.dto.CommentActionMetaDTO;
import com.questnr.model.entities.CommentAction;
import com.questnr.model.entities.LikeAction;
import com.questnr.model.entities.LikeCommentAction;
import com.questnr.model.entities.User;
import com.questnr.services.user.UserCommonService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentActionMetaMapper {

    public static CommentActionMetaDTO getMetaMapper(final CommentAction commentAction, UserCommonService userCommonService) {
        try {
            Long userId = userCommonService.getUserId();
            CommentActionMetaDTO commentActionMetaDTO = new CommentActionMetaDTO();
            List<LikeCommentAction> likeCommentActionList = commentAction.getLikeCommentActionSet().stream().filter(likeCommentAction ->
                    likeCommentAction.getUserActor().getUserId().equals(userId)
            ).collect(Collectors.toList());
            if (likeCommentActionList.size() == 1) {
                commentActionMetaDTO.setLiked(true);
            }
            return commentActionMetaDTO;
        } catch (Exception e) {
            return new CommentActionMetaDTO();
        }
    }
}
