package com.questnr.model.repositories;

import com.questnr.model.entities.FAQClassification;
import com.questnr.model.entities.FAQItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FAQItemRepository extends JpaRepository<FAQItem, Long> {
    boolean existsByFaqClassificationAndQuestion(FAQClassification faqClassification, String question);

    FAQItem findFirstByFaqClassificationAndQuestion(FAQClassification faqClassification, String question);

    @Query("select distinct faq from FAQItem faq where LOWER(faq.question) like %:q% " +
            " OR LOWER(faq.answer) like %:q%")
    Page<FAQItem> findByQuestionContaining(@Param("q") String q, Pageable pageable);

    Page<FAQItem> findByFaqClassification(FAQClassification faqClassification, Pageable pageable);
}
