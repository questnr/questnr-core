package com.questnr.model.entities;

import org.springframework.stereotype.Indexed;

import javax.persistence.*;

@Entity
@Table(name = "qr_static_interest")
@Indexed
public class StaticInterest {

    @Id
    @Column(name = "static_interest_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "static_interest_seq")
    @SequenceGenerator(name = "static_interest_seq", sequenceName = "static_interest_seq", allocationSize = 1)
    private Long staticInterestId;

    @Column(name = "interest", unique = true)
    private String interest;

    public StaticInterest() {
    }

    public StaticInterest(String interest) {
        this.interest = interest;
    }

    public Long getStaticInterestId() {
        return staticInterestId;
    }

    public void setStaticInterestId(Long staticInterestId) {
        this.staticInterestId = staticInterestId;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
