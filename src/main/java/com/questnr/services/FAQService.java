package com.questnr.services;

import com.questnr.exceptions.InvalidRequestException;
import com.questnr.model.dto.faq.FAQItemDTO;
import com.questnr.model.dto.faq.FAQItemPageDTO;
import com.questnr.model.entities.FAQClassification;
import com.questnr.model.entities.FAQItem;
import com.questnr.model.mapper.FAQItemMapper;
import com.questnr.model.repositories.FAQClassificationRepository;
import com.questnr.model.repositories.FAQItemRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FAQService {
    @Autowired
    FAQClassificationRepository faqClassificationRepository;

    @Autowired
    FAQItemRepository faqItemRepository;

    @Autowired
    FAQItemMapper faqItemMapper;

    public FAQService() {
        this.faqItemMapper = Mappers.getMapper(FAQItemMapper.class);
    }

    public Page<FAQItemDTO> searchFAQItem(String q, Pageable pageable) {
        if(q != null && q.length() > 0){
            Page<FAQItem> faqItemPage = faqItemRepository.findByQuestionContaining(q.toLowerCase().trim(), pageable);
            return new PageImpl<>(faqItemMapper.toDTOs(faqItemPage.getContent()),
                    pageable,
                    faqItemPage.getTotalElements());
        }
        throw new InvalidRequestException("Invalid query");
    }

    public Page<FAQItemPageDTO> getFAQItems(Long classificationId, Pageable pageable){
        FAQClassification faqClassification =
                faqClassificationRepository.findByFaqClassificationId(classificationId);
        Page<FAQItem> faqItemPage = faqItemRepository.findByFaqClassification(faqClassification, pageable);
        return new PageImpl<>(faqItemMapper.toPageDTOs(faqItemPage.getContent()),
                pageable,
                faqItemPage.getTotalElements());
    }
}