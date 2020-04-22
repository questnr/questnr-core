package com.questnr.model.entities;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Indexed
@Table(name = "qr_user_notifications")
@EntityListeners(AuditingEntityListener.class)
public class Notification extends DomainObject {

    @Id
    @Column(name = "user_notification_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_notification_seq")
    @SequenceGenerator(name = "user_notification_seq", sequenceName = "user_notification_seq", allocationSize = 1)
    private Long notificationId;

    @ManyToOne
    User user;

    @Any(
            metaColumn = @Column(name = "notification_type")
    )
    @AnyMetaDef(metaType = "string", idType = "long",
            metaValues = {
                    @MetaValue(value = "L", targetEntity = LikeAction.class),
                    @MetaValue(value = "C", targetEntity = CommentAction.class),
                    @MetaValue(value = "LC", targetEntity = LikeCommentAction.class),
                    @MetaValue(value = "I", targetEntity = CommunityInvitedUser.class)
            }
    )
    @JoinColumn(name = "notification_base_id")
    private NotificationBase notificationBase;

    private boolean isRead;

    public Notification() {
        this.addMetadata();
    }

    public Notification(LikeAction likeAction) {
        this.user = likeAction.getPostAction().getUserActor();
        this.notificationBase = likeAction;
//        this.notificationType = NotificationType.like;
        this.isRead = false;
        this.addMetadata();
    }

    public Notification(CommentAction commentAction) {
        this.user = commentAction.getPostAction().getUserActor();
        this.notificationBase = commentAction;
//        this.notificationType = NotificationType.comment;
        this.isRead = false;
        this.addMetadata();
    }

    public Notification(LikeCommentAction likeCommentAction) {
        this.user = likeCommentAction.getCommentAction().getPostAction().getUserActor();
        this.notificationBase = likeCommentAction;
//        this.notificationType = NotificationType.likeComment;
        this.isRead = false;
        this.addMetadata();
    }

    public Notification(CommunityInvitedUser communityInvitedUser) {
        this.user = communityInvitedUser.getUser();
        this.notificationBase = communityInvitedUser;
//        this.notificationType = NotificationType.invitation;
        this.isRead = false;
        this.addMetadata();
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public NotificationBase getNotificationBase() {
        return notificationBase;
    }

    public void setNotificationBase(NotificationBase notificationBase) {
        this.notificationBase = notificationBase;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}