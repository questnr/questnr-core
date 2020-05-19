package com.questnr.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRequest {

    @NotBlank(message = "Username is mandatory")
    @Pattern(regexp = "^[_A-z0-9]*$", message = "Username is invalid")
    @Size(min = 3, max = 32, message = "Username should contain maximum of 32 characters and at least 3 characters")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, max = 100, message = "Password requirements don't match")
    private String password;

    @NotBlank(message = "First name is mandatory")
    @Pattern(regexp = "^[\\S]*$", message = "First name is invalid")
    @Size(min = 3, max = 25, message = "First name should contain maximum of 25 characters and at least 3 characters")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Pattern(regexp = "^[\\S]*$", message = "Last name is invalid")
    @Size(min = 3, max = 25, message = "First name should contain maximum of 25 characters and at least 3 characters")
    private String lastName;

    @NotBlank(message = "Username is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9]+[a-zA-Z0-9.-]+[a-zA-Z0-9]+\\.[a-z]{1,3}$",
            message = "Email is invalid")
    private String emailId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
