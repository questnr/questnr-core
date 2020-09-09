package com.questnr.services.admin;

import com.questnr.exceptions.InvalidRequestException;
import com.questnr.model.entities.FAQClassification;
import com.questnr.model.entities.FAQItem;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.FAQClassificationRepository;
import com.questnr.model.repositories.FAQItemRepository;
import com.questnr.requests.FAQClassificationRequest;
import com.questnr.requests.FAQItemRequest;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FAQAdminService {
    @Autowired
    UserCommonService userCommonService;

    @Autowired
    FAQClassificationRepository faqClassificationRepository;

    @Autowired
    FAQItemRepository faqItemRepository;

    public FAQClassification createFAQClassification(FAQClassificationRequest faqClassificationRequest) {
        User admin = userCommonService.getUser();
        String category = faqClassificationRequest.getCategory();
        if (category != null) {
            if (!faqClassificationRepository.existsByCategory(category)) {
                FAQClassification faqClassification = new FAQClassification();
                faqClassification.setAdmin(admin);
                faqClassification.setCategory(category);
                faqClassification.setDescription(faqClassificationRequest.getDescription());
                faqClassification.addMetadata();
                return faqClassificationRepository.save(faqClassification);
            } else {
                return faqClassificationRepository.findFirstByCategory(category);
            }
        } else {
            throw new InvalidRequestException("Invalid category");
        }
    }

    public FAQItem createFAQItem(FAQItemRequest faqItemRequest) {
        User admin = userCommonService.getUser();
        FAQClassification faqClassification = faqClassificationRepository.findByFaqClassificationId(faqItemRequest.getClassificationId());
        if (faqClassification != null) {
            if (!faqItemRepository.existsByFaqClassificationAndQuestion(faqClassification,
                    faqItemRequest.getQuestion())) {
                FAQItem faqItem = new FAQItem();
                faqItem.setAdmin(admin);
                faqItem.setFaqClassification(faqClassification);
                faqItem.setQuestion(faqItemRequest.getQuestion().trim());
                faqItem.setAnswer(faqItemRequest.getAnswer().trim());
                faqItem.addMetadata();
                return faqItemRepository.save(faqItem);
            } else {
                return faqItemRepository.findFirstByFaqClassificationAndQuestion(faqClassification,
                        faqItemRequest.getQuestion());
            }
        } else {
            throw new InvalidRequestException("Invalid FAQ Classification");
        }
    }
}
