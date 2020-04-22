package com.questnr.model.dto;

import com.questnr.common.enums.NotificationType;

public class NotificationDTO {
    private Integer notificationId;

    private UserDTO userActor;

    private CommunityForPostActionDTO community;

    private PostMediaDTO postMedia;

    private String message;

    private NotificationType notificationType;

    private boolean isOpened;

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public UserDTO getUserActor() {
        return userActor;
    }

    public void setUserActor(UserDTO userActor) {
        this.userActor = userActor;
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
