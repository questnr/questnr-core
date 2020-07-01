package com.questnr.model.dto.post.question;

import com.questnr.common.enums.PostPollAnswerType;

public class PostPollQuestionMetaDTO {

    private PostPollAnswerType pollAnswer;

    private int totalAnswered;

    public PostPollAnswerType getPollAnswer() {
        return pollAnswer;
    }

    public void setPollAnswer(PostPollAnswerType pollAnswer) {
        this.pollAnswer = pollAnswer;
    }

    public int getTotalAnswered() {
        return totalAnswered;
    }

    public void setTotalAnswered(int totalAnswered) {
        this.totalAnswered = totalAnswered;
    }
}
