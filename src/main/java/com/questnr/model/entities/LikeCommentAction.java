package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.questnr.common.NotificationTitles;
import com.questnr.common.enums.NotificationType;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;

@Entity
@Indexed
@Table(name = "qr_like_comment_actions")
public class LikeCommentAction extends DomainObject implements NotificationBase {
    @Id
    @Column(name = "like_comment_action_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "like_comment_action_seq")
    @SequenceGenerator(name = "like_comment_action_seq", sequenceName = "like_comment_action_seq", allocationSize = 1)
    private Long likeCommentActionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userActor;

    @JsonIgnoreProperties("likeCommentActionSet")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_action_id", nullable = false)
    private CommentAction commentAction;

    public Long getLikeCommentActionId() {
        return likeCommentActionId;
    }

    public void setLikeCommentActionId(Long likeCommentActionId) {
        this.likeCommentActionId = likeCommentActionId;
    }

    @JsonIgnore
    public CommentAction getCommentAction() {
        return commentAction;
    }

    public void setCommentAction(CommentAction commentAction) {
        this.commentAction = commentAction;
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
        return NotificationType.likeComment;
    }

    @JsonIgnore
    public String getNotificationTitles() {
        return NotificationTitles.LIKE_COMMENT_ACTION;
    }
}
