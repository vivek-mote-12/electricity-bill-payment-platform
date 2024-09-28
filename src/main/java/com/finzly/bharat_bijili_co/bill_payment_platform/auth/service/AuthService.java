package com.finzly.bharat_bijili_co.bill_payment_platform.auth.service;

import com.finzly.bharat_bijili_co.bill_payment_platform.auth.dto.OtpVerificationRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.auth.utils.JwtUtil;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.FailedToSendEmailException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private EmailService emailService;
    private OtpService otpService;

    public AuthService(EmailService emailService,OtpService otpService){
        this.emailService=emailService;
        this.otpService=otpService;
    }

    public String login(String username){
        String otp = otpService.generateOtp();
        otpService.storeOtp(username, otp);

        try {
            emailService.sendOtpEmail(username, otp);
            return "OTP sent to your email. It will expire in 5 Minutes";
        } catch (Exception e) {
            otpService.clearOtp(username); // Clear OTP if email sending fails
            throw new FailedToSendEmailException("Failed to send email");
        }
    }

    public String verifyOtp(OtpVerificationRequest otpVerificationRequest){
        String storedOtp = otpService.getStoredOtp(otpVerificationRequest.getUsername());

        if (storedOtp != null && storedOtp.equals(otpVerificationRequest.getOtp())) {
            otpService.clearOtp(otpVerificationRequest.getUsername());  // Clear OTP after successful verification
            String token = JwtUtil.generateToken(otpVerificationRequest.getUsername(), otpVerificationRequest.getRoles());
            return token;
        } else {
            throw new FailedToSendEmailException("Invalid or Expired OTP!");
        }
    }
}
