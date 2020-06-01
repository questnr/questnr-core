package com.questnr.controllers;

import com.questnr.requests.OTPVerificationRequest;
import com.questnr.requests.UserEmailRequest;
import com.questnr.responses.BooleanResponse;
import com.questnr.services.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class OTPController {
    @Autowired
    OTPService otpService;

    @RequestMapping(value = "/send-otp", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void sendOTP(@RequestBody UserEmailRequest userEmailRequest) {
        otpService.sendOTP(userEmailRequest);
    }

    @RequestMapping(value = "/resend-otp", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void resendOTP(@RequestBody UserEmailRequest userEmailRequest) {
        otpService.resendOTP(userEmailRequest);
    }

    @RequestMapping(value = "/verify-otp", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    BooleanResponse verifyOTP(@RequestBody OTPVerificationRequest otpVerificationRequest) {
        return otpService.verifyOTP(otpVerificationRequest);
    }
}
