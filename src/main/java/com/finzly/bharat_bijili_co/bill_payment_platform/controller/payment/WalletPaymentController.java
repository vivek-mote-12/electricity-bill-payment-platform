package com.finzly.bharat_bijili_co.bill_payment_platform.controller.payment;

import com.finzly.bharat_bijili_co.bill_payment_platform.controller.customerCardDetails.CustomerCardDetailsController;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.CardPaymentRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.CreateCustomerCardDetailsRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.RecordPaymentRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.WalletPaymentRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.response.GenericResponse;
import com.finzly.bharat_bijili_co.bill_payment_platform.enums.PaymentStatus;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.InvalidOrExpiredOtpException;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Wallet;
import com.finzly.bharat_bijili_co.bill_payment_platform.service.payment.PaymentProcessingService;
import com.finzly.bharat_bijili_co.bill_payment_platform.service.wallet.WalletService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
public class WalletPaymentController {

    private final WalletService walletService;
    private final PaymentDatabaseController paymentDatabaseController;
    private final PaymentProcessingService paymentProcessingService;
    private final CustomerCardDetailsController customerCardDetailsController;

    WalletPaymentController(WalletService walletService, PaymentDatabaseController paymentDatabaseController, PaymentProcessingService paymentProcessingService, CustomerCardDetailsController customerCardDetailsController) {
        this.walletService = walletService;
        this.paymentDatabaseController = paymentDatabaseController;
        this.paymentProcessingService = paymentProcessingService;
        this.customerCardDetailsController = customerCardDetailsController;
    }

    @PostMapping("/pay_using_wallet")
    public ResponseEntity<?> payUsingWallet(@RequestBody @Valid WalletPaymentRequest walletPaymentRequest) {
        Wallet wallet = walletService.payUsingWallet(walletPaymentRequest);

        RecordPaymentRequest recordPaymentRequest = RecordPaymentRequest.builder()
                .billId(walletPaymentRequest.getBillId())
                .amount(walletPaymentRequest.getAmount())
                .paymentMethod(walletPaymentRequest.getPaymentMethod())
                .paymentStatus(walletPaymentRequest.getPaymentStatus())
                .txnRefId("WALLET" + UUID.randomUUID().toString().replace("-", "").substring(0, 10))
                .build();

        if(walletPaymentRequest.getPaymentStatus() == PaymentStatus.FAILED){
            paymentDatabaseController.recordPayment(recordPaymentRequest);
            return new ResponseEntity<>(new GenericResponse<>("Wallet Balance is Insufficient Please Add balance to Wallet",wallet), HttpStatus.BAD_REQUEST);
        }

        return paymentDatabaseController.recordPayment(recordPaymentRequest);
    }

    @GetMapping("/{customerId}/balance")
    public ResponseEntity<?> getWalletBalance(@PathVariable("customerId") String customerId) {
//        System.out.println("wallet balance fetched");
        Wallet wallet = walletService.getWalletBalance(customerId);
        return new ResponseEntity<>(new GenericResponse<>("Wallet Balance Retreived successfully ",wallet),
                HttpStatus.OK);
    }


    @PutMapping("/{customerId}/addBalance")
    public ResponseEntity<?> addBalance(@PathVariable("customerId") String customerId, @RequestBody @Valid BigDecimal amount) {
        Wallet wallet = walletService.addBalance(customerId,amount);
        return new ResponseEntity<>(new GenericResponse<>("Balance added to Wallet",wallet),HttpStatus.OK);
    }

    @PostMapping("/topUpRequest")
    public ResponseEntity<?> topUp(@RequestBody @Valid CardPaymentRequest cardPaymentRequest) {
        String customerId = cardPaymentRequest.getCustomerId();
        CreateCustomerCardDetailsRequest createCustomerCardDetailsRequest = cardPaymentRequest.getCustomerCardDetails();
        Double amount = cardPaymentRequest.getBillAmount();
        paymentProcessingService.initiatePayment(customerId, amount);
        return customerCardDetailsController.createCustomerCardDetails(cardPaymentRequest.getCustomerCardDetails());
    }

    public static class TopUpRequestObj{
        @NotBlank
        String customerId;

        BigDecimal amount;

        public BigDecimal getAmount() {
            return amount;
        }
        public String getCustomerId() {
            return customerId;
        }
    }
    @PostMapping("/topUpVerify")
    public ResponseEntity<?> topUpVerify(@RequestParam String OTP,@RequestBody TopUpRequestObj topUpRequestObj) {
        Wallet wallet;
        if(paymentProcessingService.verifyOTP(OTP)){
            wallet = walletService.addBalance(topUpRequestObj.getCustomerId(),topUpRequestObj.getAmount());
        }
        else {
            wallet = walletService.getWalletBalance(topUpRequestObj.getCustomerId());
            throw new InvalidOrExpiredOtpException("Invalid Otp");
        }
        return new ResponseEntity<>(new GenericResponse<>("Balance added to Wallet",wallet),HttpStatus.OK);
    }

}
