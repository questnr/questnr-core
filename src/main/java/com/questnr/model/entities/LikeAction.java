package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.questnr.common.NotificationTitles;
import com.questnr.common.enums.NotificationType;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;

@Entity
@Indexed
@Table(name = "qr_like_actions")
public class LikeAction extends DomainObject implements NotificationBase {
    @Id
    @Column(name = "like_action_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "like_action_seq")
    @SequenceGenerator(name = "like_action_seq", sequenceName = "like_action_seq", allocationSize = 1)
    private Long likeActionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userActor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_action_id", nullable = false)
    private PostAction postAction;

    public Long getLikeActionId() {
        return likeActionId;
    }

    public void setLikeActionId(Long likeActionId) {
        this.likeActionId = likeActionId;
    }

    @JsonIgnore
    public PostAction getPostAction() {
        return postAction;
    }

    public void setPostAction(PostAction postAction) {
        this.postAction = postAction;
    }

    @JsonIgnore
    public User getUserActor() {
        return userActor;
    }

    public void setUserActor(User userActor) {
        this.userActor = userActor;
    }

    @JsonIgnore
    public NotificationType getNotificationType() {
        return NotificationType.like;
    }

    @JsonIgnore
    public String getNotificationTitles() {
        return NotificationTitles.LIKE_ACTION;
    }
}
