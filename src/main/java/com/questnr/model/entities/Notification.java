package com.questnr.model.entities;

import com.questnr.common.enums.NotificationFunctionality;
import com.questnr.common.enums.NotificationType;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.MetaValue;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.EnumBridge;
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
                    @MetaValue(value = "P", targetEntity = PostAction.class),
                    @MetaValue(value = "L", targetEntity = LikeAction.class),
                    @MetaValue(value = "C", targetEntity = CommentAction.class),
                    @MetaValue(value = "LC", targetEntity = LikeCommentAction.class),
                    @MetaValue(value = "I", targetEntity = CommunityInvitedUser.class),
                    @MetaValue(value = "FC", targetEntity = CommunityUser.class),
                    @MetaValue(value = "FU", targetEntity = UserFollower.class),
                    @MetaValue(value = "PA", targetEntity = PostPollAnswer.class),
                    @MetaValue(value = "RC", targetEntity = CommunityUserRequest.class)
            }
    )
    @Cascade({org.hibernate.annotations.CascadeType.MERGE})
    @JoinColumn(name = "notification_base_id")
    private NotificationBase notificationBase;

    private boolean isRead;

    @Field(bridge = @FieldBridge(impl = EnumBridge.class), store = Store.YES)
    @Column(name = "functionality", columnDefinition = "varchar default 'normal'")
    @Enumerated(EnumType.STRING)
    private NotificationFunctionality notificationFunctionality;

    public Notification() {
        this.addMetadata();
    }

    public Notification(PostPollAnswer postPollAnswer) {
        this.user = postPollAnswer.getPostAction().getUserActor();
        this.notificationBase = postPollAnswer;
        this.isRead = false;
        this.addMetadata();
        this.setNotificationFunctionality(NotificationFunctionality.answer);
    }

    public Notification(PostAction postAction, User user) {
        this.user = user;
        this.notificationBase = postAction;
        this.isRead = false;
        this.addMetadata();
        this.setNotificationFunctionality(NotificationFunctionality.normal);
    }

    public Notification(LikeAction likeAction) {
        this.user = likeAction.getPostAction().getUserActor();
        this.notificationBase = likeAction;
        this.isRead = false;
        this.addMetadata();
        this.setNotificationFunctionality(NotificationFunctionality.normal);
    }

    public Notification(CommentAction commentAction) {
        if (commentAction.isChildComment()) {
            CommentAction parentCommentAction = commentAction.getParentCommentAction();
            this.user = parentCommentAction.getUserActor();
        } else {
            this.user = commentAction.getPostAction().getUserActor();
        }
        this.notificationBase = commentAction;
        this.isRead = false;
        this.addMetadata();
        this.setNotificationFunctionality(NotificationFunctionality.normal);
    }

    public Notification(LikeCommentAction likeCommentAction) {
        this.user = likeCommentAction.getCommentAction().getPostAction().getUserActor();
        this.notificationBase = likeCommentAction;
        this.isRead = false;
        this.addMetadata();
    }

    public Notification(CommunityInvitedUser communityInvitedUser) {
        this.user = communityInvitedUser.getUser();
        this.notificationBase = communityInvitedUser;
        this.isRead = false;
        this.addMetadata();
        this.setNotificationFunctionality(NotificationFunctionality.normal);
    }

    public Notification(CommunityUser communityUser) {
        if (communityUser.getNotificationType() == NotificationType.followedCommunity) {
            this.user = communityUser.getCommunity().getOwnerUser();
            this.notificationBase = communityUser;
            this.isRead = false;
            this.addMetadata();
            this.setNotificationFunctionality(NotificationFunctionality.normal);
        } else if (communityUser.getNotificationType() == NotificationType.communityAccepted) {
            this.user = communityUser.getUser();
            this.notificationBase = communityUser;
            this.isRead = false;
            this.addMetadata();
            this.setNotificationFunctionality(NotificationFunctionality.normal);
        }
    }

    public Notification(UserFollower userFollower) {
        this.user = userFollower.getUser();
        this.notificationBase = userFollower;
        this.isRead = false;
        this.addMetadata();
        this.setNotificationFunctionality(NotificationFunctionality.normal);
    }

    public Notification(CommunityUserRequest communityUserRequest) {
        this.user = communityUserRequest.getCommunity().getOwnerUser();
        this.notificationBase = communityUserRequest;
        this.isRead = false;
        this.addMetadata();
        this.setNotificationFunctionality(NotificationFunctionality.normal);
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

    public NotificationFunctionality getNotificationFunctionality() {
        return notificationFunctionality;
    }

    public void setNotificationFunctionality(NotificationFunctionality notificationFunctionality) {
        this.notificationFunctionality = notificationFunctionality;
    }
}