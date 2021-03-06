package com.questnr.model.dto.faq;

import com.questnr.model.dto.user.UserInternalDTO;

public class FAQClassificationDTO {
    private Long faqClassificationId;

    private String category;

    private String description;

    private UserInternalDTO admin;

    public Long getFaqClassificationId() {
        return faqClassificationId;
    }

    public void setFaqClassificationId(Long faqClassificationId) {
        this.faqClassificationId = faqClassificationId;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserInternalDTO getAdmin() {
        return admin;
    }

    public void setAdmin(UserInternalDTO admin) {
        this.admin = admin;
    }
}
