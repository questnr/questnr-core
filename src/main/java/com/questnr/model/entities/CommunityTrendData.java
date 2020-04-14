package com.questnr.model.entities;


import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "qr_community_trend_data")
@Indexed
public class CommunityTrendData {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "community_data_seq")
    @SequenceGenerator(name = "community_data", sequenceName = "community_data_seq", allocationSize = 1)
    private Long communityTrendDataId;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    @Column(name = "rank", length = 50)
    private Double rank;

    @Column(name = "observed_date", length = 50)
    private Date observedDate;


    public Long getCommunityTrendDataId() {
        return communityTrendDataId;
    }

    public void setCommunityTrendDataId(Long communityTrendDataId) {
        this.communityTrendDataId = communityTrendDataId;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
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
