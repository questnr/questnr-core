package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Set;

@Entity
@Indexed
@Table(name = "qr_comment_actions")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CommentAction {
    @Id
    @Column(name = "comment_action_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_action_seq")
    @SequenceGenerator(name = "comment_action_seq", sequenceName = "comment_action_seq", allocationSize = 1)
    private Long commentActionId;

    @Column(name = "comment_object")
    private  String commentObject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userActor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_action_id", nullable = false)
    private PostAction postAction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_comment_action_id", referencedColumnName="comment_action_id")
    private CommentAction parentCommentAction;

    public Long getCommentActionId() {
        return commentActionId;
    }

    public void setCommentActionId(Long commentActionId) {
        this.commentActionId = commentActionId;
    }

    public String getCommentObject() {
        return commentObject;
    }

    public void setCommentObject(String commentObject) {
        this.commentObject = commentObject;
    }

    public User getUserActor() {
        return userActor;
    }

    public void setUserActor(User userActor) {
        this.userActor = userActor;
    }

    public PostAction getPostAction() {
        return postAction;
    }

    public void setPostAction(PostAction postAction) {
        this.postAction = postAction;
    }


    public CommentAction getParentCommentAction() {
        return parentCommentAction;
    }

    public void setParentCommentAction(CommentAction parentCommentAction) {
        this.parentCommentAction = parentCommentAction;
    }
}
