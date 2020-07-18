package com.questnr.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

public class UserUpdateRequest {

    @NotBlank(message = "Username is mandatory")
    @Pattern(regexp = "^[_A-z0-9]*$", message = "Username is invalid")
    @Size(min = 3, max = 32, message = "Username should contain maximum of 32 characters and at least 3 characters")
    private String username;

    @NotBlank(message = "First name is mandatory")
    @Pattern(regexp = "^[\\S]*$", message = "First name is invalid")
    @Size(min = 3, max = 25, message = "First name should contain maximum of 25 characters and at least 3 characters")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Pattern(regexp = "^[\\S]*$", message = "Last name is invalid")
    @Size(min = 3, max = 25, message = "First name should contain maximum of 25 characters and at least 3 characters")
    private String lastName;

    private String bio;

    private Date dob;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase();
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }
}
