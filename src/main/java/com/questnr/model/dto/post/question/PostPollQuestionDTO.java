package com.questnr.model.dto.post.question;

import com.questnr.model.dto.post.PostBaseDTO;

public class PostPollQuestionDTO extends PostBaseDTO {
    private String questionText;
    private PollQuestionDTO pollQuestion;
    private PostPollQuestionMetaDTO pollQuestionMeta;

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public PollQuestionDTO getPollQuestion() {
        return pollQuestion;
    }

    public void setPollQuestion(PollQuestionDTO pollQuestion) {
        this.pollQuestion = pollQuestion;
    }

    public PostPollQuestionMetaDTO getPollQuestionMeta() {
        return pollQuestionMeta;
    }

    public void setPollQuestionMeta(PostPollQuestionMetaDTO pollQuestionMeta) {
        this.pollQuestionMeta = pollQuestionMeta;
    }
}
