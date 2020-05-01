package com.questnr.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "qr_user_notification_settings")
public class UserNotificationSettings {
    @Id
    @Column(name = "user_notification_setting_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_notification_setting_seq")
    @SequenceGenerator(name = "user_notification_setting_seq", sequenceName = "user_notification_setting_seq", allocationSize = 1)
    private Long userNotificationSettingsId;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_receiving_notification")
    private boolean receivingNotification = false;

    public Long getUserNotificationSettingsId() {
        return userNotificationSettingsId;
    }

    public void setUserNotificationSettingsId(Long userNotificationSettingsId) {
        this.userNotificationSettingsId = userNotificationSettingsId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isReceivingNotification() {
        return receivingNotification;
    }

    public void setReceivingNotification(boolean receivingNotification) {
        this.receivingNotification = receivingNotification;
    }
}
