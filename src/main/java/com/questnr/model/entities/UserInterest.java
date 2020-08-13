package com.questnr.model.entities;

import org.springframework.stereotype.Indexed;

import javax.persistence.*;

@Entity
@Table(name = "qr_user_interests")
@Indexed
public class UserInterest {

    @Id
    @Column(name = "user_interest_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_interest_seq")
    @SequenceGenerator(name = "user_interest_seq", sequenceName = "user_interest_seq", allocationSize = 1)
    private Long userInterestId;

    @ManyToOne
    @JoinColumn(name = "static_interest_id")
    private StaticInterest staticInterest;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserInterest() {
    }

    public Long getUserInterestId() {
        return userInterestId;
    }

    public void setUserInterestId(Long userInterestId) {
        this.userInterestId = userInterestId;
    }

    public StaticInterest getStaticInterest() {
        return staticInterest;
    }

    public void setStaticInterest(StaticInterest staticInterest) {
        this.staticInterest = staticInterest;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
