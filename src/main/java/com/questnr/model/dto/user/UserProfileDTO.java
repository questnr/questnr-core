package com.questnr.model.dto.user;

import java.util.Date;

public class UserProfileDTO extends UserDTO {

    private Date dob;

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }
}
