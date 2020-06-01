package com.questnr.model.entities;


import org.springframework.stereotype.Indexed;

import javax.persistence.*;

@Entity
@Table(name = "qr_email_otp_sent")
@Indexed
public class EmailOTPSent extends DomainObject {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qr_email_otp_sent_seq")
    @SequenceGenerator(name = "qr_email_otp_sent_seq", sequenceName = "qr_email_otp_sent_seq", allocationSize = 1)
    private Long emailOTPSentId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "otp")
    private String otp;

    public Long getEmailOTPSentId() {
        return emailOTPSentId;
    }

    public void setEmailOTPSentId(Long emailOTPSentId) {
        this.emailOTPSentId = emailOTPSentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
