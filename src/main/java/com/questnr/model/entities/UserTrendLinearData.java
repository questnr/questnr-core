package com.questnr.model.entities;

import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "qr_user_trend_linear_data")
@Indexed
public class UserTrendLinearData {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_trend_linear_data_seq")
    @SequenceGenerator(name = "user_trend_linear_data", sequenceName = "user_trend_linear_data_seq", allocationSize = 1)
    private Long userTrendLinearDataId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Transient
    private List<Double> x = new ArrayList<>();

    @Transient
    private List<Double> y = new ArrayList<>();

    @Column(name = "slop")
    private double slop;

    @Column(name = "trend_rank")
    private Integer trendRank;

    @Column(name = "points")
    private Double points = Double.valueOf(0);

    public Long getUserTrendLinearDataId() {
        return userTrendLinearDataId;
    }

    public void setUserTrendLinearDataId(Long userTrendLinearDataId) {
        this.userTrendLinearDataId = userTrendLinearDataId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Integer getTrendRank() {
        return trendRank;
    }

    public void setTrendRank(Integer trendRank) {
        this.trendRank = trendRank;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public void addPoints(Double points) {
        this.points += points;
    }

    public void addX(Double xEle) {
        this.x.add(xEle);
    }

    public void addY(Double yEle) {
        this.y.add(yEle);
    }
}
