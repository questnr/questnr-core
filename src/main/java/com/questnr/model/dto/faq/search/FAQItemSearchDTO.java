package com.questnr.model.dto.faq.search;

public class FAQItemSearchDTO {
    private FAQClassificationSearchDTO faqClassification;

    private String question;

    private String answer;

    public FAQClassificationSearchDTO getFaqClassification() {
        return faqClassification;
    }

    public void setFaqClassification(FAQClassificationSearchDTO faqClassification) {
        this.faqClassification = faqClassification;
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
