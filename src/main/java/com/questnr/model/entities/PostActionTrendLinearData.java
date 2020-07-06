package com.questnr.model.entities;

import com.questnr.common.enums.PostType;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.EnumBridge;
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

    @Field(bridge = @FieldBridge(impl = EnumBridge.class), store = Store.YES)
    @Column(name = "post_type", columnDefinition = "varchar default 'simple'")
    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Column(name = "slop")
    private double slop;

    @Column(name = "trend_rank")
    private Integer trendRank;

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

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
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

    public void addX(Double xEle) {
        this.x.add(xEle);
    }

    public void addY(Double yEle) {
        this.y.add(yEle);
    }
}
