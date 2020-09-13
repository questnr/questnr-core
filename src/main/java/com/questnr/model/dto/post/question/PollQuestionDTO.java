package com.questnr.model.dto.post.question;

public class PollQuestionDTO {
    private String agreeText;

    private String disagreeText;

    public String getAgreeText() {
        return agreeText;
    }

    public void setAgreeText(String agreeText) {
        this.agreeText = agreeText;
    }

    public String getDisagreeText() {
        return disagreeText;
    }

    public void setDisagreeText(String disagreeText) {
        this.disagreeText = disagreeText;
    }
}
