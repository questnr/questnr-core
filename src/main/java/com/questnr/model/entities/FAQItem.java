package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Indexed
@Table(name = "qr_faq_items")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FAQItem extends DomainObject {

    @Id
    @Column(name = "faq_item_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "faq_item_seq")
    @SequenceGenerator(name = "faq_item_seq", sequenceName = "faq_item_seq", allocationSize = 1)
    private Long faqItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faq_classification_id", nullable = false)
    private FAQClassification faqClassification;

    @Column(name = "question")
    private String question;

    @Column(name = "answer", columnDefinition = "TEXT")
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    public Long getFaqItemId() {
        return faqItemId;
    }

    public void setFaqItemId(Long faqItemId) {
        this.faqItemId = faqItemId;
    }

    public FAQClassification getFaqClassification() {
        return faqClassification;
    }

    public void setFaqClassification(FAQClassification faqClassification) {
        this.faqClassification = faqClassification;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
