package com.questnr.model.entities;


import com.questnr.common.enums.NotificationType;

public interface NotificationBase {
    NotificationType getNotificationType();

    String getNotificationTitles();
}
