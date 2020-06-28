package com.questnr.model.dto.post.question;

import com.questnr.common.enums.PostPollAnswerType;

public class PostPollQuestionMetaDTO {

    private PostPollAnswerType pollAnswer;

    public PostPollAnswerType getPollAnswer() {
        return pollAnswer;
    }

    public void setPollAnswer(PostPollAnswerType pollAnswer) {
        this.pollAnswer = pollAnswer;
    }
}
