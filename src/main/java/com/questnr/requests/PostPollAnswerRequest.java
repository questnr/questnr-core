package com.questnr.requests;

import com.questnr.common.enums.PostPollAnswerType;

public class PostPollAnswerRequest {

    private PostPollAnswerType pollAnswer;

    public PostPollAnswerType getPollAnswer() {
        return pollAnswer;
    }

    public void setPollAnswer(PostPollAnswerType pollAnswer) {
        this.pollAnswer = pollAnswer;
    }
}
