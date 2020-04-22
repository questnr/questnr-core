package com.questnr.services.notification;

import com.questnr.model.entities.*;
import com.questnr.model.repositories.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationJob {

    @Autowired
    NotificationRepository notificationRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public void createNotificationJob(Notification notification) {
        this.createNotificationJob(notification, true);
    }

    public void createNotificationJob(Notification notification, boolean toCreate) {
        if (toCreate) NotificationProcessor.getInstance(notificationRepository).add(notification);
        else NotificationRevertProcessor.getInstance(notificationRepository).add(notification);
    }

    public void createNotificationJob(LikeAction likeAction, boolean toCreate) {
        Notification notification = new Notification(likeAction);
        this.createNotificationJob(notification, toCreate);
    }

    public void createNotificationJob(CommentAction commentAction, boolean toCreate) {
        Notification notification = new Notification(commentAction);
        this.createNotificationJob(notification, toCreate);
    }

    public void createNotificationJob(LikeCommentAction likeCommentAction, boolean toCreate) {
        Notification notification = new Notification(likeCommentAction);
        this.createNotificationJob(notification, toCreate);
    }

    public void createNotificationJob(CommunityInvitedUser communityInvitedUser, boolean toCreate) {
        Notification notification = new Notification(communityInvitedUser);
        this.createNotificationJob(notification, toCreate);
    }

    public void createNotificationJob(CommunityUser communityUser, boolean toCreate) {
        Notification notification = new Notification(communityUser);
        this.createNotificationJob(notification, toCreate);
    }

    public void createNotificationJob(UserFollower userFollower, boolean toCreate) {
        Notification notification = new Notification(userFollower);
        this.createNotificationJob(notification, toCreate);
    }


    public void createNotificationJob(LikeAction likeAction) {
        this.createNotificationJob(likeAction, true);
    }

    public void createNotificationJob(CommentAction commentAction) {
        this.createNotificationJob(commentAction, true);
    }

    public void createNotificationJob(LikeCommentAction likeCommentAction) {
        this.createNotificationJob(likeCommentAction, true);
    }

    public void createNotificationJob(CommunityInvitedUser communityInvitedUser) {
        this.createNotificationJob(communityInvitedUser, true);
    }

    public void createNotificationJob(CommunityUser communityUser) {
        this.createNotificationJob(communityUser, true);
    }

    public void createNotificationJob(UserFollower userFollower) {
        this.createNotificationJob(userFollower, true);
    }
}
