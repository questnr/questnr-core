package com.questnr.requests;

import javax.validation.constraints.NotNull;

public class PushNotificationTokenRegistryRequest {
    @NotNull
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
