package com.questnr.requests;

import javax.validation.constraints.NotBlank;

public class PostPollQuestionRequest {

    private Long postId;

    @NotBlank(message = "Text is mandatory")
    private String text;
    @NotBlank(message = "Agree Text is mandatory")
    private String agreeText;
    @NotBlank(message = "Disagree Text is mandatory")
    private String disagreeText;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

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
