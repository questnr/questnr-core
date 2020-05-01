package com.questnr.model.dto;

public class UserNotificationSettingsDTO {
    private boolean receivingNotification;

    public boolean isReceivingNotification() {
        return receivingNotification;
    }

    public void setReceivingNotification(boolean receivingNotification) {
        this.receivingNotification = receivingNotification;
    }
}
