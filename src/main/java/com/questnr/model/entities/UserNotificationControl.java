package com.questnr.model.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "qr_user_notification_control")
public class UserNotificationControl {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_notification_control_seq")
    @SequenceGenerator(name = "user_notification_control_seq", sequenceName = "user_notification_control_seq", allocationSize = 1)
    private Long userNotificationControlId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userActor;

    @Column(name = "token", columnDefinition = "TEXT")
    private String token;

    public Long getUserNotificationControlId() {
        return userNotificationControlId;
    }

    public void setUserNotificationControlId(Long userNotificationControlId) {
        this.userNotificationControlId = userNotificationControlId;
    }

    public User getUserActor() {
        return userActor;
    }

    public void setUserActor(User userActor) {
        this.userActor = userActor;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserNotificationControl that = (UserNotificationControl) o;
        return token.equals(that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
