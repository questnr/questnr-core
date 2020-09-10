package com.questnr.model.dto.faq.search;

import com.questnr.services.CommonService;

public class FAQClassificationSearchDTO {
    private String category;

    private String description;

    public String getCategory() {
        return CommonService.titleCase(this.category);
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
}
