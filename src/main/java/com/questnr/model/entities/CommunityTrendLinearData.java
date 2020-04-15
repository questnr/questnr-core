package com.questnr.model.entities;

import com.questnr.model.entities.Community;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "qr_community_trend_linear_data")
@Indexed
public class CommunityTrendLinearData {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "community_trend_linear_data_seq")
    @SequenceGenerator(name = "community_trend_linear_data", sequenceName = "community_trend_linear_data_seq", allocationSize = 1)
    private Long communityTrendLinerDataId;

    @OneToOne
    @JoinColumn(name = "community_id")
    private Community community;

    @Transient
    private List<Double> x = new ArrayList<>();

    @Transient
    private List<Double> y = new ArrayList<>();

    @Column(name = "slop")
    private double slop;

    public Long getCommunityTrendLinerDataId() {
        return communityTrendLinerDataId;
    }

    public void setCommunityTrendLinerDataId(Long communityTrendLinerDataId) {
        this.communityTrendLinerDataId = communityTrendLinerDataId;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
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

    public void addX(Double xEle){
        this.x.add(xEle);
    }

    public void addY(Double yEle){
        this.y.add(yEle);
    }
}
