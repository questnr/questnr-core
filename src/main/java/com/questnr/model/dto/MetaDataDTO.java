package com.questnr.model.dto;

public class MetaDataDTO {
    private String timeString;

    private String actionDate;

    private String actionDateForPost;

    private String createdAtTimeInUTC;

    private Long createdAtTimestamp;

    private String updatedAtTimeInUTC;

    private Long updatedAtTimestamp;

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

    public String getCreatedAtTimeInUTC() {
        return createdAtTimeInUTC;
    }

    public void setCreatedAtTimeInUTC(String createdAtTimeInUTC) {
        this.createdAtTimeInUTC = createdAtTimeInUTC;
    }

    public Long getCreatedAtTimestamp() {
        return createdAtTimestamp;
    }

    public void setCreatedAtTimestamp(Long createdAtTimestamp) {
        this.createdAtTimestamp = createdAtTimestamp;
    }

    public String getUpdatedAtTimeInUTC() {
        return updatedAtTimeInUTC;
    }

    public void setUpdatedAtTimeInUTC(String updatedAtTimeInUTC) {
        this.updatedAtTimeInUTC = updatedAtTimeInUTC;
    }

    public Long getUpdatedAtTimestamp() {
        return updatedAtTimestamp;
    }

    public void setUpdatedAtTimestamp(Long updatedAtTimestamp) {
        this.updatedAtTimestamp = updatedAtTimestamp;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }
}
