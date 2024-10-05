package com.finzly.bharat_bijili_co.bill_payment_platform.service.payment;

import com.finzly.bharat_bijili_co.bill_payment_platform.auth.dto.OtpVerificationRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.auth.service.EmailService;
import com.finzly.bharat_bijili_co.bill_payment_platform.auth.service.OtpService;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.FailedToSendEmailException;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.NotAbleToSendOtpException;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.customer.CustomerRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PaymentProcessingService {

    private final CustomerRepository customerRepository;
    private final EmailService emailService;
    private final OtpService otpService;

    PaymentProcessingService(CustomerRepository customerRepository, EmailService emailService, OtpService otpService) {
        this.customerRepository = customerRepository;
        this.emailService = emailService;
        this.otpService = otpService;
    }

    public void initiatePayment(String customerId, Double billAmount) {

        String otp = otpService.generateOtp();
        otpService.storeOtp(customerId,otp);
        String toEmail = customerRepository.findEmailByCustomerId(customerId);

        try{
            emailService.sendOtpEmail(toEmail,otp);
        } catch (Exception e){
            otpService.clearOtp(customerId);
            throw new FailedToSendEmailException("Failed to send OTP email");
        }
    }

    public Boolean verifyOTP(OtpVerificationRequest otpVerificationRequest)
    {
        String storedOTP = otpService.getStoredOtp(otpVerificationRequest.getUsername());
        if(storedOTP != null && storedOTP.equals(otpVerificationRequest.getOtp())){
            otpService.clearOtp(otpVerificationRequest.getUsername());
            return true;
        }
        else {
            throw new FailedToSendEmailException("Invalid or Expired OTP");
        }
    }
}
