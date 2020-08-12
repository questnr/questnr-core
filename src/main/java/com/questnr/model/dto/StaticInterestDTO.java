package com.questnr.model.dto;

import com.questnr.model.entities.StaticInterest;

public class StaticInterestDTO {
    private String interest;

    public StaticInterestDTO() {
    }

    public StaticInterestDTO(String interest) {
        this.interest = interest;
    }

    public StaticInterestDTO(StaticInterest staticInterest) {
        this.interest = staticInterest.getInterest();
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
