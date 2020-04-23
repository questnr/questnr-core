package com.questnr.model.entities;

import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "qr_hash_tag_trend_linear_data")
@Indexed
public class HashTagTrendLinearData {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hash_tag_trend_linear_data_seq")
    @SequenceGenerator(name = "hash_tag_trend_linear_data", sequenceName = "hash_tag_trend_linear_data_seq", allocationSize = 1)
    private Long hashTagTrendLinearDataId;

    @OneToOne
    @JoinColumn(name = "hash_tag_id")
    private HashTag hashTag;

    @Transient
    private List<Double> x = new ArrayList<>();

    @Transient
    private List<Double> y = new ArrayList<>();

    @Column(name = "slop")
    private double slop;

    public Long getHashTagTrendLinearDataId() {
        return hashTagTrendLinearDataId;
    }

    public void setHashTagTrendLinearDataId(Long hashTagTrendLinearDataId) {
        this.hashTagTrendLinearDataId = hashTagTrendLinearDataId;
    }

    public HashTag getHashTag() {
        return hashTag;
    }

    public void setHashTag(HashTag hashTag) {
        this.hashTag = hashTag;
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
