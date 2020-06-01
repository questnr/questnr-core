package com.questnr.model.repositories;

import com.questnr.model.entities.EmailOTPSent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailOTPSentRepository extends JpaRepository<EmailOTPSent, Long> {

  EmailOTPSent findByEmail(String email);

  boolean existsByEmailAndOtp(String email, String otp);
}
