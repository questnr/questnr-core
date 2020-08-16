package com.questnr.requests;

public class UserActivityRequest {
    private Long entityId;
    private Long trackingId;
    private String referrer;
    private boolean updateRequest;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(Long trackingId) {
        this.trackingId = trackingId;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public boolean isUpdateRequest() {
        return updateRequest;
    }

    public void setUpdateRequest(boolean updateRequest) {
        this.updateRequest = updateRequest;
    }
}
