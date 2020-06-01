package com.questnr.services;

import com.questnr.exceptions.InvalidRequestException;
import com.questnr.model.entities.EmailOTPSent;
import com.questnr.model.repositories.EmailOTPSentRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.requests.OTPVerificationRequest;
import com.questnr.requests.UserEmailRequest;
import com.questnr.responses.BooleanResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OTPService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    EmailOTPSentRepository emailOTPSentRepository;

    @Autowired
    CommonService commonService;

    final int MAX_NUMBER_OF_EMAIL_PER_DAY = 4;

    final int ONE_DAY = 86400 * 1000;

    private boolean hasCrossedMaxLimitRequest(UserEmailRequest userEmailRequest) throws InvalidRequestException{
        Date endingDate = new Date();
        Date startingDate = new Date(endingDate.getTime() - ONE_DAY);
        int numberOfHasBeenSentToday = emailOTPSentRepository.countAllByEmailAndCreatedAtBetween(userEmailRequest.getEmail(), startingDate, endingDate);
        if(numberOfHasBeenSentToday > MAX_NUMBER_OF_EMAIL_PER_DAY) {
            throw new InvalidRequestException("You have crossed maximum limit per day!");
        }
        return false;
    }

    public void sendOTP(UserEmailRequest userEmailRequest){
        if(!this.hasCrossedMaxLimitRequest(userEmailRequest)) {
            EmailOTPSent emailOTPSent = new EmailOTPSent();
            emailOTPSent.setEmail(userEmailRequest.getEmail());
            emailOTPSent.addMetadata();
            emailOTPSent.setOtp(commonService.getRandomNumber());
            EmailOTPSent savedEmailOTPSent = emailOTPSentRepository.save(emailOTPSent);
            emailService.sendOTPEmail(savedEmailOTPSent);
        }
    }

    public void resendOTP(UserEmailRequest userEmailRequest){
        if(!this.hasCrossedMaxLimitRequest(userEmailRequest)) {
            List<EmailOTPSent> emailOTPSentList = emailOTPSentRepository.findByEmailOrderByCreatedAtDesc(userEmailRequest.getEmail());
            if (emailOTPSentList.size() == 0) {
                EmailOTPSent emailOTPSent = new EmailOTPSent();
                emailOTPSent.addMetadata();
                emailOTPSent.setEmail(userEmailRequest.getEmail());
                emailOTPSent.setOtp(commonService.getRandomNumber());
                EmailOTPSent savedEmailOTPSent = emailOTPSentRepository.save(emailOTPSent);
                emailService.sendOTPEmail(savedEmailOTPSent);
            } else {
                EmailOTPSent reEmailOTPSent = new EmailOTPSent();
                reEmailOTPSent.addMetadata();
                reEmailOTPSent.setEmail(userEmailRequest.getEmail());
                reEmailOTPSent.setOtp(emailOTPSentList.get(0).getOtp());
                EmailOTPSent savedEmailOTPSent = emailOTPSentRepository.save(reEmailOTPSent);
                emailService.sendOTPEmail(savedEmailOTPSent);
            }
        }
    }

    public BooleanResponse verifyOTP(OTPVerificationRequest otpVerificationRequest){
        BooleanResponse booleanResponse = new BooleanResponse();
        try{
            List<EmailOTPSent> emailOTPSentList = emailOTPSentRepository.findByEmailOrderByCreatedAtDesc(otpVerificationRequest.getEmail());
            if(emailOTPSentList.size() != 0){
                if(otpVerificationRequest.getOtp().equals(emailOTPSentList.get(0).getOtp())){
                    booleanResponse.setStatus(true);
                }
            }
            return booleanResponse;
        }catch (Exception e){
            throw new InvalidRequestException("Please, try again!");
        }
    }
}
