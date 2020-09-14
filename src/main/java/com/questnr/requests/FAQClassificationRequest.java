package com.questnr.requests;

import javax.validation.constraints.NotBlank;

public class FAQClassificationRequest {
    @NotBlank(message = "category is mandatory")
    private String category;

    @NotBlank(message = "description is mandatory")
    private String description;

    public String getCategory() {
        if (category != null)
            return category.trim();
        return null;
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
