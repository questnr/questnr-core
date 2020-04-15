package com.questnr.model.entities;


import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "qr_post_action_trend_data")
@Indexed
public class PostActionTrendData {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_action_trend_data_seq")
    @SequenceGenerator(name = "post_action_trend_data", sequenceName = "post_action_trend_data_seq", allocationSize = 1)
    private Long postActionTrendDataId;

    @ManyToOne
    @JoinColumn(name = "post_action_id")
    private PostAction postAction;

    @Column(name = "rank", length = 50)
    private Double rank;

    @Column(name = "observed_date", length = 50)
    private Date observedDate;

    public Long getPostActionTrendDataId() {
        return postActionTrendDataId;
    }

    public void setPostActionTrendDataId(Long postActionTrendDataId) {
        this.postActionTrendDataId = postActionTrendDataId;
    }

    public PostAction getPostAction() {
        return postAction;
    }

    public void setPostAction(PostAction postAction) {
        this.postAction = postAction;
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
