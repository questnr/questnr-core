package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Indexed
@Table(name = "qr_faq_classifications")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FAQClassification extends DomainObject {

    @Id
    @Column(name = "faq_classification_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "faq_classification_seq")
    @SequenceGenerator(name = "faq_classification_seq",
            sequenceName = "faq_classification_seq",
            allocationSize = 1,
    initialValue = 1001)
    private Long faqClassificationId;

    @Column(name = "category")
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    public Long getFaqClassificationId() {
        return faqClassificationId;
    }

    public void setFaqClassificationId(Long faqClassificationId) {
        this.faqClassificationId = faqClassificationId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }
}
