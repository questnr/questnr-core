package com.questnr.model.entities;

import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "qr_post_action_trend_linear_data")
@Indexed
public class PostActionTrendLinearData {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_action_trend_linear_data_seq")
    @SequenceGenerator(name = "post_action_trend_linear_data", sequenceName = "post_action_trend_linear_data_seq", allocationSize = 1)
    private Long postActionTrendLinearDataId;

    @OneToOne
    @JoinColumn(name = "post_action_id")
    private PostAction postAction;

    @Transient
    private List<Double> x = new ArrayList<>();

    @Transient
    private List<Double> y = new ArrayList<>();

    @Column(name = "slop")
    private double slop;

    public Long getPostActionTrendLinearDataId() {
        return postActionTrendLinearDataId;
    }

    public void setPostActionTrendLinearDataId(Long postActionTrendLinearDataId) {
        this.postActionTrendLinearDataId = postActionTrendLinearDataId;
    }

    public PostAction getPostAction() {
        return postAction;
    }

    public void setPostAction(PostAction postAction) {
        this.postAction = postAction;
    }

    public List<Double> getX() {
        return x;
    }

    public void setX(List<Double> x) {
        this.x = x;
    }

    public List<Double> getY() {
        return y;
    }

    public void setY(List<Double> y) {
        this.y = y;
    }

    public double getSlop() {
        return slop;
    }

    public void setSlop(double slop) {
        this.slop = slop;
    }

    public void addX(Double xEle) {
        this.x.add(xEle);
    }

    public void addY(Double yEle) {
        this.y.add(yEle);
    }
}
