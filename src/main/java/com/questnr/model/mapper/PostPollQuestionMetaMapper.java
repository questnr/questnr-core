package com.questnr.model.mapper;

import com.questnr.common.enums.PostType;
import com.questnr.model.dto.post.question.PostPollQuestionMetaDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostPollAnswer;
import com.questnr.services.user.UserCommonService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostPollQuestionMetaMapper {

    public static PostPollQuestionMetaDTO getMetaMapper(final PostAction postAction, final UserCommonService userCommonService) {
        if (postAction.getPostType() == PostType.question) {
            try {
                Long userId = userCommonService.getUserId();
                PostPollQuestionMetaDTO postPollQuestionMetaDTO = new PostPollQuestionMetaDTO();
                List<PostPollAnswer> postPollAnswerList = postAction.getPostPollQuestion().getPostPollAnswer().stream().filter(postPollAnswer ->
                        postPollAnswer.getUserActor().getUserId().equals(userId)
                ).collect(Collectors.toList());
                if (postPollAnswerList.size() == 1) {
                    postPollQuestionMetaDTO.setPollAnswer(postPollAnswerList.get(0).getAnswer());
                }
                return postPollQuestionMetaDTO;
            } catch (Exception e) {
                return new PostPollQuestionMetaDTO();
            }
        }
        return new PostPollQuestionMetaDTO();
    }
}
