package com.questnr.services.ses.enums;

public enum SESFrom {

    BRIJESH("brijeshlakkad22@questnr.com", "Brijesh from Questnr"),
    AMAN("amanchoudharyys@questnr.com", "Aman from Questnr"),
    SATISH("satish.k.gaur2009@questnr.com", "Satish from Questnr"),

    // @Todo Change NO_REPLY to noreply@questnr.com
    NO_REPLY("noreply@questnr.com", "Questnr"),
//    NO_REPLY("brijeshlakkad22@gmail.com", "Questnr"),

    SUPPORT("support@questnr.com", "Questnr Support");

    private final String email;
    private final String name;

    private SESFrom(String email, String name) {
        this.email =email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}