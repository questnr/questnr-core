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

    public void sendOTP(UserEmailRequest userEmailRequest){
        try{
            EmailOTPSent emailOTPSent = emailOTPSentRepository.findByEmail(userEmailRequest.getEmail());
            if(emailOTPSent != null){
                emailOTPSent.updateMetadata();
            }else{
                emailOTPSent = new EmailOTPSent();
                emailOTPSent.setEmail(userEmailRequest.getEmail());
                emailOTPSent.addMetadata();
            }
            emailOTPSent.setOtp(commonService.getRandomNumber());
            EmailOTPSent savedEmailOTPSent = emailOTPSentRepository.save(emailOTPSent);
            emailService.sendOTPEmail(savedEmailOTPSent);
        }catch (Exception e){
            throw new InvalidRequestException("Please, try again!");
        }
    }

    public void resendOTP(UserEmailRequest userEmailRequest){
        try{
            EmailOTPSent emailOTPSent = emailOTPSentRepository.findByEmail(userEmailRequest.getEmail());
            if(emailOTPSent == null){
                emailOTPSent.addMetadata();
                emailOTPSent.setOtp(commonService.getRandomNumber());
                EmailOTPSent savedEmailOTPSent = emailOTPSentRepository.save(emailOTPSent);
                emailService.sendOTPEmail(savedEmailOTPSent);
            }
            else{
                emailService.sendOTPEmail(emailOTPSent);
            }
        }catch (Exception e){
            throw new InvalidRequestException("Please, try again!");
        }
    }

    public BooleanResponse verifyOTP(OTPVerificationRequest otpVerificationRequest){
        BooleanResponse booleanResponse = new BooleanResponse();
        try{
            EmailOTPSent emailOTPSent = emailOTPSentRepository.findByEmail(otpVerificationRequest.getEmail());
            if(emailOTPSent != null){
                if(otpVerificationRequest.getOtp().equals(emailOTPSent.getOtp())){
                    booleanResponse.setStatus(true);
                }
            }
            return booleanResponse;
        }catch (Exception e){
            throw new InvalidRequestException("Please, try again!");
        }
    }
}
