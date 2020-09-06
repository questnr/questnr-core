package com.questnr.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class FAQItemRequest {
    @NotBlank(message = "classification id is mandatory")
    private Long classificationId;

    @NotBlank(message = "question is mandatory")
    @Size(min = 5, max = 400, message = "invalid question")
    private String question;

    @NotBlank(message = "answer is mandatory")
    @Size(min = 5, message = "invalid answer")
    private String answer;

    public Long getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(Long classificationId) {
        this.classificationId = classificationId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
