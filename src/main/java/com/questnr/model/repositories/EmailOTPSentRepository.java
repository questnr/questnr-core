package com.questnr.model.repositories;

import com.questnr.model.entities.EmailOTPSent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface EmailOTPSentRepository extends JpaRepository<EmailOTPSent, Long> {

  EmailOTPSent findByEmail(String email);

  List<EmailOTPSent> findByEmailOrderByCreatedAtDesc(String email);

  List<EmailOTPSent> findAllByEmailAndCreatedAtBetween(String email, Date startingDate, Date endingDate);

  int countAllByEmailAndCreatedAtBetween(String email, Date startingDate, Date endingDate);

  boolean existsByEmailAndOtp(String email, String otp);
}
