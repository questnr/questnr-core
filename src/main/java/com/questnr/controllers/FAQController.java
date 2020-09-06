package com.questnr.controllers;

import com.questnr.model.dto.faq.FAQItemDTO;
import com.questnr.model.dto.faq.FAQItemPageDTO;
import com.questnr.services.FAQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class FAQController {

    @Autowired
    FAQService faqService;

    @RequestMapping(value = "/search/faq", method = RequestMethod.GET)
    public Page<FAQItemDTO> searchFAQ(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "5") int size,
                                      @RequestParam(defaultValue = "") String q) {
        Pageable pageable = PageRequest.of(page, size);
        return faqService.searchFAQItem(q, pageable);
    }

    @RequestMapping(value = "/faq/{classificationId}", method = RequestMethod.GET)
    public Page<FAQItemPageDTO> getFAQItems(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "all") String size,
                                            @PathVariable Long classificationId) {
        Pageable pageable;
        if (size.equals("all")) {
            pageable = PageRequest.of(page, Integer.MAX_VALUE);
        } else {
            int sizeInt = 0;
            try {
                sizeInt = Integer.parseInt(size);
                pageable = PageRequest.of(page, sizeInt);
            } catch (Exception e) {
                pageable = PageRequest.of(page, Integer.MAX_VALUE);
            }
        }
        return faqService.getFAQItems(classificationId, pageable);
    }
}
