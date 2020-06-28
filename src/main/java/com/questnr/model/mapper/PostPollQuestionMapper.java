package com.questnr.model.mapper;

import com.questnr.common.enums.PostPollAnswerType;
import com.questnr.model.dto.post.question.PollQuestionDTO;
import com.questnr.model.entities.PostPollAnswer;
import com.questnr.model.entities.PostPollQuestion;
import com.questnr.requests.PostPollQuestionRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostPollQuestionMapper {

    public PollQuestionDTO toDTO(PostPollQuestion pollQuestionAction) {
        PollQuestionDTO pollQuestionDTO = new PollQuestionDTO();
        if(pollQuestionAction != null) {
            List<PostPollAnswer> postPollAnswerList = pollQuestionAction.getPostPollAnswer();
            List<PostPollAnswer> agreePostPollAnswers = postPollAnswerList.stream().filter(postPollAnswer ->
                    postPollAnswer.getAnswer() == PostPollAnswerType.agree
            ).collect(Collectors.toList());
            int totalCount = postPollAnswerList.size();
            int positiveCount = agreePostPollAnswers.size();
            int negativeCount = totalCount - agreePostPollAnswers.size();
            if(totalCount > 0) {
                pollQuestionDTO.setAgreePercentage(((double) positiveCount / totalCount) * 100);
                pollQuestionDTO.setDisagreePercentage(((double) negativeCount / totalCount * 100));
            }
            pollQuestionDTO.setAgreeText(pollQuestionAction.getAgreeText());
            pollQuestionDTO.setDisagreeText(pollQuestionAction.getDisagreeText());
        }
        return pollQuestionDTO;
    }

    public PostPollQuestion fromRequest(PostPollQuestionRequest postPollQuestionRequest){
        PostPollQuestion pollQuestionAction = new PostPollQuestion();
        pollQuestionAction.setAgreeText(postPollQuestionRequest.getAgreeText());
        pollQuestionAction.setDisagreeText(postPollQuestionRequest.getDisagreeText());
        return pollQuestionAction;
    }
}
