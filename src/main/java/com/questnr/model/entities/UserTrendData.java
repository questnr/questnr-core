package com.questnr.model.entities;


import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "qr_user_trend_data")
@Indexed
public class UserTrendData {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_trend_data_seq")
    @SequenceGenerator(name = "user_trend_data", sequenceName = "user_trend_data_seq", allocationSize = 1)
    private Long userTrendDataId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "rank", length = 50)
    private Double rank;

    @Column(name = "observed_date", length = 50)
    private Date observedDate;

    public Long getUserTrendDataId() {
        return userTrendDataId;
    }

    public void setUserTrendDataId(Long userTrendDataId) {
        this.userTrendDataId = userTrendDataId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getRank() {
        return rank;
    }

    public void setRank(Double rank) {
        this.rank = rank;
    }

    public Date getObservedDate() {
        return observedDate;
    }

    public void setObservedDate(Date observedDate) {
        this.observedDate = observedDate;
    }
}
