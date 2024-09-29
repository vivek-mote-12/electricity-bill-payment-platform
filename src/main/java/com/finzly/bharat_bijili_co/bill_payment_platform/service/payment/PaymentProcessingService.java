package com.finzly.bharat_bijili_co.bill_payment_platform.service.payment;

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
//    private static String generatedOtp;

    private final ConcurrentHashMap<String, String> otpStore = new ConcurrentHashMap<>();

//    private CreateCustomerCardDetailsRequest createCustomerCardDetailsRequest;
    private Customer customer;
    private final CustomerRepository customerRepository;

    @Autowired
    private JavaMailSender mailSender;

    PaymentProcessingService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void initiatePayment(String custId, Double billAmount) {
//        this.createCustomerCardDetailsRequest = createCustomerCardDetailsRequest;
//        String customerId = custId;

        // Logic to Send Otp
        Random random = new Random();
        int otpNum = 100000 + random.nextInt(900000);  // Generates a 6-digit OTP
        String generatedOtp = String.valueOf(otpNum);
        otpStore.put("a", generatedOtp);

        String toEmail = customerRepository.findEmailByCustomerId(custId);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("your-email@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Your OTP Code");
            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "  body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
                    "  .container { max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                    "  .header { background-color: #4CAF50; padding: 10px 0; text-align: center; color: white; border-top-left-radius: 8px; border-top-right-radius: 8px; }" +
                    "  h1 { margin: 0; font-size: 24px; }" +
                    "  .content { padding: 20px; text-align: center; }" +
                    "  .otp-code { font-size: 32px; color: #333333; font-weight: bold; margin: 20px 0; }" +
                    "  .btn { display: inline-block; background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-size: 16px; }" +
                    "  .btn:hover { background-color: #45a049; }" +
                    "  .footer { text-align: center; padding: 20px; color: #777777; font-size: 14px; }" +
                    "  .footer a { color: #4CAF50; text-decoration: none; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "  <div class='container'>" +
                    "    <div class='header'>" +
                    "      <h1>BBC : Your OTP Code</h1>" +
                    "    </div>" +
                    "    <div class='content'>" +
                    "      <p>Dear Customer,</p>" +
                    "      <p>Please use the following One-Time Password (OTP) to complete your Payment :</p>" +
                    "      <div class='otp-code'>" + generatedOtp + "</div>" +
                    "      <p>This code is valid for 10 minutes.</p>" +
                    "      Login Now" +
                    "    </div>" +
                    "    <div class='footer'>" +
                    "      <p>Thank you for using our service.</p>" +
                    "      <p>For support, visit our <a href='#'>Help Center</a>.</p>" +
                    "    </div>" +
                    "  </div>" +
                    "</body>" +
                    "</html>";
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
//            exception handler
            throw new NotAbleToSendOtpException("Not Able To Send Otp Please Try Again !!");
//            throw new RuntimeException(e);
        }
    }

    public Boolean verifyOTP(String OTPCode)
    {
//        System.out.println("verify otp method : otp from mail :  " + OTPCode);
//        System.out.println("verify otp method : prev otp : " + generatedOtp);
        String storedOtp = otpStore.get("a");
        if(storedOtp != null && OTPCode.equals(storedOtp)){
            //Record Payment to database
            System.out.println("Verify OTP verified");
//            generatedOtp = null;
            return true;
        }
        return false;
    }
}
