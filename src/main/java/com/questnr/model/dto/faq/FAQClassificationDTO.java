package com.questnr.model.dto.faq;

import com.questnr.model.dto.user.UserInternalDTO;
import com.questnr.services.CommonService;

public class FAQClassificationDTO {
    private Long faqClassificationId;

    private String category;

    private UserInternalDTO admin;

    public Long getFaqClassificationId() {
        return faqClassificationId;
    }

    public void setFaqClassificationId(Long faqClassificationId) {
        this.faqClassificationId = faqClassificationId;
    }

    public String getCategory() {
        return CommonService.titleCase(this.category);
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public UserInternalDTO getAdmin() {
        return admin;
    }

    public void setAdmin(UserInternalDTO admin) {
        this.admin = admin;
    }
}
