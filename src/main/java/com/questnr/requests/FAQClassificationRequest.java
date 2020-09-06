package com.questnr.requests;

import javax.validation.constraints.NotBlank;

public class FAQClassificationRequest {
    @NotBlank(message = "category is mandatory")
    private String category;

    public String getCategory() {
        if (category != null)
            return category.toLowerCase().trim();
        return null;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
