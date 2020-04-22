package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_action_id", nullable = false)
    private CommentAction commentAction;

    public Long getLikeCommentActionId() {
        return likeCommentActionId;
    }

    public void setLikeCommentActionId(Long likeCommentActionId) {
        this.likeCommentActionId = likeCommentActionId;
    }

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
}
