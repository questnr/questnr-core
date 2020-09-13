package com.questnr.model.dto.post.question;

import com.questnr.common.enums.PostPollAnswerType;

public class PostPollQuestionMetaDTO {
    private PostPollAnswerType pollAnswer;

    private Long totalAnswered;

    private Double agreePercentage = (double) 0;

    private Double disagreePercentage = (double) 0;

    public PostPollAnswerType getPollAnswer() {
        return pollAnswer;
    }

    public void setPollAnswer(PostPollAnswerType pollAnswer) {
        this.pollAnswer = pollAnswer;
    }

    public Long getTotalAnswered() {
        return totalAnswered;
    }

    public void setTotalAnswered(Long totalAnswered) {
        this.totalAnswered = totalAnswered;
    }

    public Double getAgreePercentage() {
        return agreePercentage;
    }

    public void setAgreePercentage(Double agreePercentage) {
        this.agreePercentage = agreePercentage;
    }

    public Double getDisagreePercentage() {
        return disagreePercentage;
    }

    public void setDisagreePercentage(Double disagreePercentage) {
        this.disagreePercentage = disagreePercentage;
    }
}
