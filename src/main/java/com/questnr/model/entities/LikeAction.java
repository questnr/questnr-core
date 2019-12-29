package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.search.annotations.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Indexed
@Table(name = "qr_like_actions")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LikeAction {
    @Id
    @Column(name = "like_action_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "like_action_seq")
    @SequenceGenerator(name = "like_action_seq", sequenceName = "like_action_seq", allocationSize = 1)
    private int likeActionId;

    @OneToOne
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_action_id", nullable = false)
    private PostAction postAction;

    public int getLikeActionId() {
        return likeActionId;
    }

    public void setLikeActionId(int likeActionId) {
        this.likeActionId = likeActionId;
    }

    public PostAction getPostAction() {
        return postAction;
    }

    public void setPostAction(PostAction postAction) {
        this.postAction = postAction;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
