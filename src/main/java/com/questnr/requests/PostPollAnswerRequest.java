package com.questnr.requests;

import com.questnr.common.enums.PostPollAnswerType;

public class PostPollAnswerRequest {

    private Long postId;

    private PostPollAnswerType pollAnswer;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public PostPollAnswerType getPollAnswer() {
        return pollAnswer;
    }

    public void setPollAnswer(PostPollAnswerType pollAnswer) {
        this.pollAnswer = pollAnswer;
    }
}
