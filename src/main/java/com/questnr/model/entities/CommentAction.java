package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.questnr.common.NotificationTitles;
import com.questnr.common.enums.NotificationType;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Indexed
@Table(name = "qr_comment_actions")
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "deleted = false")
public class CommentAction extends DomainObject implements NotificationBase {
    @Id
    @Column(name = "comment_action_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_action_seq")
    @SequenceGenerator(name = "comment_action_seq", sequenceName = "comment_action_seq", allocationSize = 1)
    private Long commentActionId;

    @Column(name = "deleted", nullable = false, columnDefinition = "bool default false")
    private boolean deleted;

    @Column(name = "comment_object")
    private String commentObject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userActor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_action_id", nullable = false)
    private PostAction postAction;

    @OneToMany(mappedBy = "parentCommentAction", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommentAction> childCommentSet;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private CommentAction parentCommentAction;

    @Column(name = "is_child_comment")
    private boolean childComment;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "commentAction", orphanRemoval = true)
    private Set<LikeCommentAction> likeCommentActionSet = new HashSet<>();

    public Long getCommentActionId() {
        return commentActionId;
    }

    public void setCommentActionId(Long commentActionId) {
        this.commentActionId = commentActionId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getCommentObject() {
        return commentObject;
    }

    public void setCommentObject(String commentObject) {
        this.commentObject = commentObject;
    }

    @JsonIgnore
    public User getUserActor() {
        return userActor;
    }

    public void setUserActor(User userActor) {
        this.userActor = userActor;
    }

    @JsonIgnore
    public PostAction getPostAction() {
        return postAction;
    }

    public void setPostAction(PostAction postAction) {
        this.postAction = postAction;
    }

    public Set<CommentAction> getChildCommentSet() {
        return childCommentSet;
    }

    public void setChildCommentSet(Set<CommentAction> childCommentSet) {
        this.childCommentSet = childCommentSet;
    }

    public boolean isChildComment() {
        return childComment;
    }

    public void setChildComment(boolean childComment) {
        this.childComment = childComment;
    }

    @JsonIgnore
    public CommentAction getParentCommentAction() {
        return parentCommentAction;
    }

    public void setParentCommentAction(CommentAction parentCommentAction) {
        this.parentCommentAction = parentCommentAction;
    }

    public Set<LikeCommentAction> getLikeCommentActionSet() {
        return likeCommentActionSet;
    }

    public void setLikeCommentActionSet(Set<LikeCommentAction> likeCommentActionSet) {
        this.likeCommentActionSet = likeCommentActionSet;
    }

    @JsonIgnore
    public NotificationType getNotificationType() {
        return NotificationType.comment;
    }

    @JsonIgnore
    public String getNotificationTitles() {
        return NotificationTitles.COMMENT_ACTION;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentAction that = (CommentAction) o;
        return commentActionId.equals(that.commentActionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentActionId);
    }
}
