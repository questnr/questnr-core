package com.questnr.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.questnr.common.enums.NotificationType;
import com.questnr.model.entities.User;

public class NotificationDTO {
    private Long notificationId;

    private UserDTO userActor;

    private User user;

    private CommunityForPostActionDTO community;

    private PostMediaDTO postMedia;

    private String message;

    private NotificationType notificationType;

    private boolean isOpened;

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public UserDTO getUserActor() {
        return userActor;
    }

    public void setUserActor(UserDTO userActor) {
        this.userActor = userActor;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CommunityForPostActionDTO getCommunity() {
        return community;
    }

    public void setCommunity(CommunityForPostActionDTO community) {
        this.community = community;
    }

    public PostMediaDTO getPostMedia() {
        return postMedia;
    }

    public void setPostMedia(PostMediaDTO postMedia) {
        this.postMedia = postMedia;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }
}
