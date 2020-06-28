package com.questnr.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.questnr.common.enums.NotificationType;
import com.questnr.model.dto.community.CommunityListViewDTO;
import com.questnr.model.dto.user.UserDTO;
import com.questnr.model.entities.User;

public class NotificationDTO {
    private Long notificationId;

    private UserDTO userActor;

    private User user;

    private CommunityListViewDTO community;

    private PostMediaDTO postMedia;

    private String message;

    private NotificationType notificationType;

    private String clickAction;

    private boolean isOpened;

    private MetaDataDTO metaData;

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

    public CommunityListViewDTO getCommunity() {
        return community;
    }

    public void setCommunity(CommunityListViewDTO community) {
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

    public String getClickAction() {
        return clickAction;
    }

    public void setClickAction(String clickAction) {
        this.clickAction = clickAction;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public MetaDataDTO getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaDataDTO metaData) {
        this.metaData = metaData;
    }
}
