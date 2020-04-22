package com.questnr.services.notification;

import com.questnr.common.enums.NotificationType;
import com.questnr.model.entities.CommentAction;
import com.questnr.model.entities.LikeAction;
import com.questnr.model.entities.LikeCommentAction;
import com.questnr.model.entities.Notification;
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


    public void createNotificationJob(LikeAction likeAction) {
        Notification notification = new Notification(likeAction);
        NotificationProcessor.getInstance(notificationRepository).add(notification);
    }

    public void createNotificationJob(CommentAction commentAction) {
        Notification notification = new Notification(commentAction);
        NotificationProcessor.getInstance(notificationRepository).add(notification);
    }

    public void createNotificationJob(LikeCommentAction likeCommentAction) {
        Notification notification = new Notification(likeCommentAction);
        NotificationProcessor.getInstance(notificationRepository).add(notification);
    }
}
