package com.questnr.model.mapper;

import com.questnr.common.enums.PostPollAnswerType;
import com.questnr.model.dto.PostPollQuestionDTO;
import com.questnr.model.entities.PostPollAnswer;
import com.questnr.model.entities.PostPollQuestion;
import com.questnr.requests.PostPollQuestionRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostPollQuestionMapper {

    public PostPollQuestionDTO toDTO(PostPollQuestion pollQuestionAction) {
        PostPollQuestionDTO postPollQuestionDTO = new PostPollQuestionDTO();
        if(pollQuestionAction != null) {
            List<PostPollAnswer> postPollAnswerList = pollQuestionAction.getPostPollAnswer();
            List<PostPollAnswer> agreePostPollAnswers = postPollAnswerList.stream().filter(postPollAnswer ->
                    postPollAnswer.getAnswer() == PostPollAnswerType.agree
            ).collect(Collectors.toList());
            int totalCount = postPollAnswerList.size();
            int positiveCount = agreePostPollAnswers.size();
            int negativeCount = totalCount - agreePostPollAnswers.size();
            if(totalCount > 0) {
                postPollQuestionDTO.setAgreePercentage(((double) positiveCount / totalCount) * 100);
                postPollQuestionDTO.setDisagreePercentage(((double) negativeCount / totalCount * 100));
            }
            postPollQuestionDTO.setAgreeText(pollQuestionAction.getAgreeText());
            postPollQuestionDTO.setDisagreeText(pollQuestionAction.getDisagreeText());
        }
        return postPollQuestionDTO;
    }

    public PostPollQuestion fromRequest(PostPollQuestionRequest postPollQuestionRequest){
        PostPollQuestion pollQuestionAction = new PostPollQuestion();
        pollQuestionAction.setAgreeText(postPollQuestionRequest.getAgreeText());
        pollQuestionAction.setDisagreeText(postPollQuestionRequest.getDisagreeText());
        return pollQuestionAction;
    }
}
