package com.questnr.model.mapper;

import com.questnr.model.dto.post.question.PollQuestionDTO;
import com.questnr.model.entities.PostPollQuestion;
import com.questnr.requests.PostPollQuestionRequest;
import org.springframework.stereotype.Component;

@Component
public class PostPollQuestionMapper {
    public PollQuestionDTO toDTO(PostPollQuestion pollQuestionAction) {
        PollQuestionDTO pollQuestionDTO = new PollQuestionDTO();
        if (pollQuestionAction != null) {
            pollQuestionDTO.setAgreeText(pollQuestionAction.getAgreeText());
            pollQuestionDTO.setDisagreeText(pollQuestionAction.getDisagreeText());
        }
        return pollQuestionDTO;
    }

    public PostPollQuestion fromRequest(PostPollQuestionRequest postPollQuestionRequest) {
        PostPollQuestion pollQuestionAction = new PostPollQuestion();
        pollQuestionAction.setAgreeText(postPollQuestionRequest.getAgreeText());
        pollQuestionAction.setDisagreeText(postPollQuestionRequest.getDisagreeText());
        return pollQuestionAction;
    }
}
