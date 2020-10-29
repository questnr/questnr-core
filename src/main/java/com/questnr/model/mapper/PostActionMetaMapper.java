package com.questnr.model.mapper;

import com.questnr.model.dto.post.normal.PostActionMetaDTO;
import com.questnr.model.entities.LikeAction;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostVisit;
import com.questnr.services.user.UserCommonService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostActionMetaMapper {

    public static PostActionMetaDTO getMetaMapper(final PostAction postAction,
                                                  final UserCommonService userCommonService) {
        try {
            Long userId = userCommonService.getUserId();
            PostActionMetaDTO postActionMetaDTO = new PostActionMetaDTO();
            List<LikeAction> likeActionList = postAction.getLikeActionSet().stream().filter(likeAction ->
                    likeAction.getUserActor().getUserId().equals(userId)
            ).collect(Collectors.toList());
            List<PostVisit> postVisitList = postAction.getPostVisitSet().stream().filter(postVisit ->
                    postVisit.getUserActor().getUserId().equals(userId)
            ).collect(Collectors.toList());
            if (likeActionList.size() == 1) {
                postActionMetaDTO.setLiked(true);
            }
            if (postVisitList.size() == 1) {
                postActionMetaDTO.setVisited(true);
            }
            postActionMetaDTO.setTotalLikes(postAction.getLikeActionSet().size());
            postActionMetaDTO.setTotalComments(postAction.getCommentActionSet().size());
            postActionMetaDTO.setTotalPostVisits(postAction.getPostVisitSet().size());
            return postActionMetaDTO;
        } catch (Exception e) {
            return new PostActionMetaDTO();
        }
    }
}
