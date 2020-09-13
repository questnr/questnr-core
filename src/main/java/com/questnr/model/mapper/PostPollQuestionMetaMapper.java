package com.questnr.model.mapper;

import com.questnr.common.enums.PostPollAnswerType;
import com.questnr.common.enums.PostType;
import com.questnr.model.dto.post.question.PostPollQuestionMetaDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostPollAnswer;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.PostPollAnswerRepository;
import com.questnr.services.user.UserCommonService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostPollQuestionMetaMapper {

    public static PostPollQuestionMetaDTO getMetaMapper(final PostAction postAction,
                                                        final UserCommonService userCommonService,
                                                        final PostPollAnswerRepository postPollAnswerRepository) {
        if (postAction.getPostType() == PostType.question) {
            try {
                PostPollQuestionMetaDTO postPollQuestionMetaDTO = new PostPollQuestionMetaDTO();
                try {
                    User user = userCommonService.getUser();
                    PostPollAnswer postPollAnswer =
                            postPollAnswerRepository.findFirstByPostActionAndUserActor(postAction, user);

                    if (postPollAnswer != null) {
                        postPollQuestionMetaDTO.setPollAnswer(postPollAnswer.getAnswer());
                    }

                    if (postAction.getUserActor().equals(user) || postPollAnswer != null) {
                        List<PostPollAnswer> postPollAnswerList = postAction.getPostPollQuestion().getPostPollAnswer();
                        List<PostPollAnswer> agreePostPollAnswers = postPollAnswerList.stream().filter(postPollAnswer1 ->
                                postPollAnswer1.getAnswer() == PostPollAnswerType.agree
                        ).collect(Collectors.toList());
                        int totalCount = postPollAnswerList.size();
                        int positiveCount = agreePostPollAnswers.size();
                        int negativeCount = totalCount - agreePostPollAnswers.size();
                        if (totalCount > 0) {
                            postPollQuestionMetaDTO.setAgreePercentage(((double) positiveCount / totalCount) * 100);
                            postPollQuestionMetaDTO.setDisagreePercentage(((double) negativeCount / totalCount * 100));
                        }
                    }
                } catch (Exception e) {

                }
                postPollQuestionMetaDTO.setTotalAnswered(postPollAnswerRepository.countByPostAction(postAction));
                return postPollQuestionMetaDTO;
            } catch (Exception e) {
                return new PostPollQuestionMetaDTO();
            }
        }
        return new PostPollQuestionMetaDTO();
    }
}
