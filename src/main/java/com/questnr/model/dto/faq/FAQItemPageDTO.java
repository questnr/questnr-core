package com.questnr.model.dto.faq;

import com.questnr.services.CommonService;
import org.springframework.data.domain.Page;

public class FAQItemPageDTO {
    private String category;

    private String description;

    private Page<FAQItemDTO> faqItemPage;

    public String getCategory() {
        return CommonService.titleCase(category);
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

    public Page<FAQItemDTO> getFaqItemPage() {
        return faqItemPage;
    }

    public void setFaqItemPage(Page<FAQItemDTO> faqItemPage) {
        this.faqItemPage = faqItemPage;
    }
}
