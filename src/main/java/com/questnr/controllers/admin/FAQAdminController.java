package com.questnr.controllers.admin;

import com.questnr.model.dto.faq.FAQClassificationDTO;
import com.questnr.model.dto.faq.FAQItemAdminDTO;
import com.questnr.model.mapper.FAQClassificationMapper;
import com.questnr.model.mapper.FAQItemMapper;
import com.questnr.requests.FAQClassificationRequest;
import com.questnr.requests.FAQItemRequest;
import com.questnr.services.admin.FAQAdminService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/admin")
public class FAQAdminController {

    @Autowired
    private FAQAdminService faqAdminService;

    @Autowired
    FAQClassificationMapper faqClassificationMapper;

    @Autowired
    FAQItemMapper faqItemMapper;

    public FAQAdminController(){
        this.faqClassificationMapper = Mappers.getMapper(FAQClassificationMapper.class);
        this.faqItemMapper = Mappers.getMapper(FAQItemMapper.class);
    }

    @RequestMapping(value = "/faq/classification", method = RequestMethod.POST)
    public FAQClassificationDTO createFAQClassification(@RequestBody() FAQClassificationRequest faqClassificationRequest){
        return faqClassificationMapper.toDTO(faqAdminService.createFAQClassification(faqClassificationRequest));
    }

    @RequestMapping(value = "/faq/item", method = RequestMethod.POST)
    public FAQItemAdminDTO createFAQItem(@RequestBody() FAQItemRequest faqItemRequest){
        return faqItemMapper.toDTO(faqAdminService.createFAQItem(faqItemRequest));
    }
}