package com.finzly.bharat_bijili_co.bill_payment_platform.controller.payment;

import com.finzly.bharat_bijili_co.bill_payment_platform.controller.customerCardDetails.CustomerCardDetailsController;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.CardPaymentRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.CreateCustomerCardDetailsRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.RecordPaymentRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.enums.PaymentStatus;
import com.finzly.bharat_bijili_co.bill_payment_platform.service.customerCardDetails.CreateCardDetailsService;
import com.finzly.bharat_bijili_co.bill_payment_platform.service.payment.PaymentProcessingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/process_payment")
public class CardPaymentController {
    private final CreateCardDetailsService createCardDetailsService;
    private final PaymentProcessingService paymentProcessingService;
    private CustomerCardDetailsController customerCardDetailsController;
    private PaymentDatabaseController paymentController;
//    private final Payment payment;

    public CardPaymentController(CreateCardDetailsService createCardDetailsService,
                                 PaymentProcessingService paymentProcessingService,
                                 PaymentDatabaseController paymentDatabaseController,
                                 CustomerCardDetailsController customerCardDetailsController
                                       ) {
        this.createCardDetailsService = createCardDetailsService;
        this.paymentProcessingService = paymentProcessingService;
        this.paymentController = paymentDatabaseController;
        this.customerCardDetailsController = customerCardDetailsController;
    }

    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestBody @Valid CardPaymentRequest cardPaymentRequest) {
        String custId = cardPaymentRequest.getCustomerId();
        CreateCustomerCardDetailsRequest createCustomerCardDetailsRequest = cardPaymentRequest.getCustomerCardDetails();
        Double BillAmount = cardPaymentRequest.getBillAmount();
        paymentProcessingService.initiatePayment(custId,BillAmount);
        return customerCardDetailsController.createCustomerCardDetails(cardPaymentRequest.getCustomerCardDetails());
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestParam String OTP, @RequestBody RecordPaymentRequest recordPaymentRequest) {
        System.out.println(OTP);
//        String billid = recordPaymentRequest.getBillId();
        if(paymentProcessingService.verifyOTP(OTP)){
            System.out.println("OTP verified Successfully");
            recordPaymentRequest.setPaymentStatus(PaymentStatus.SUCCESS);
        }
        else{
            System.out.println("OTP NOT verified");
            recordPaymentRequest.setPaymentStatus(PaymentStatus.FAILED);
        }
        System.out.println(OTP);
        return paymentController.recordPayment(recordPaymentRequest);
    }
}
