package com.questnr.requests;

import javax.validation.constraints.NotNull;

public class RefreshPushNotificationTokenRegistryRequest extends PushNotificationTokenRegistryRequest {
    @NotNull
    private String expiredToken;

    public String getExpiredToken() {
        return expiredToken;
    }

    public void setExpiredToken(String expiredToken) {
        this.expiredToken = expiredToken;
    }
}
