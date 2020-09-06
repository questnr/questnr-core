package com.questnr.model.repositories;

import com.questnr.model.entities.FAQClassification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FAQClassificationRepository extends JpaRepository<FAQClassification, Long> {
    boolean existsByCategory(String category);

    FAQClassification findFirstByCategory(String category);

    FAQClassification findByFaqClassificationId(Long faqClassificationId);
}
