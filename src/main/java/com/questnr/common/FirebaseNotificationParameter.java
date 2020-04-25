package com.questnr.common;


public enum FirebaseNotificationParameter {
    SOUND("default"),
    COLOR("#FFFF00");

    private String value;

    FirebaseNotificationParameter(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
