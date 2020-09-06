package com.questnr.model.dto.faq;

import com.questnr.model.dto.user.UserInternalDTO;

public class FAQItemDTO {
    private Long faqItemId;

    private FAQClassificationDTO faqClassification;

    private String question;

    private String answer;

    private UserInternalDTO admin;

    public Long getFaqItemId() {
        return faqItemId;
    }

    public void setFaqItemId(Long faqItemId) {
        this.faqItemId = faqItemId;
    }

    public FAQClassificationDTO getFaqClassification() {
        return faqClassification;
    }

    public void setFaqClassification(FAQClassificationDTO faqClassification) {
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

    public UserInternalDTO getAdmin() {
        return admin;
    }

    public void setAdmin(UserInternalDTO admin) {
        this.admin = admin;
    }
}
