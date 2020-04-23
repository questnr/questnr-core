package com.questnr.model.entities;


import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "qr_hash_tag_trend_data")
@Indexed
public class HashTagTrendData {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hash_tag_trend_data_seq")
    @SequenceGenerator(name = "hash_tag_trend_data_seq", sequenceName = "hash_tag_trend_data_seq", allocationSize = 1)
    private Long hashTagTrendDataId;

    @ManyToOne
    @JoinColumn(name = "hash_tag_id")
    private HashTag hashTag;

    @Column(name = "rank", length = 50)
    private Double rank;

    @Column(name = "observed_date", length = 50)
    private Date observedDate;

    public Long getHashTagTrendDataId() {
        return hashTagTrendDataId;
    }

    public void setHashTagTrendDataId(Long hashTagTrendDataId) {
        this.hashTagTrendDataId = hashTagTrendDataId;
    }

    public HashTag getHashTag() {
        return hashTag;
    }

    public void setHashTag(HashTag hashTag) {
        this.hashTag = hashTag;
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
