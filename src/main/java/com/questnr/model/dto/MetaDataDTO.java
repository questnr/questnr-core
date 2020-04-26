package com.questnr.model.dto;

public class MetaDataDTO {
    private String timeString;

    private String actionDate;

    private String actionDateForPost;

    private String timeInUTC;

    private boolean isEdited;

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public String getActionDate() {
        return actionDate;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public String getActionDateForPost() {
        return actionDateForPost;
    }

    public void setActionDateForPost(String actionDateForPost) {
        this.actionDateForPost = actionDateForPost;
    }

    public String getTimeInUTC() {
        return timeInUTC;
    }

    public void setTimeInUTC(String timeInUTC) {
        this.timeInUTC = timeInUTC;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }
}
