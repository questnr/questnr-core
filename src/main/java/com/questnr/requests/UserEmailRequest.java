package com.questnr.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserEmailRequest {
    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9]+[a-zA-Z0-9.-]+[a-zA-Z0-9]+\\.[a-z]{1,3}$",
            message = "Email is invalid")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }
}
