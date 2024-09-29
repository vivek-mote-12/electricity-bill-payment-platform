package com.finzly.bharat_bijili_co.bill_payment_platform.controller.payment;

import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.RecordPaymentRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.WalletPaymentRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.response.GenericResponse;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Wallet;
import com.finzly.bharat_bijili_co.bill_payment_platform.service.wallet.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
public class WalletPaymentController {

    private final WalletService walletService;
    private final PaymentDatabaseController paymentDatabaseController;

    WalletPaymentController(WalletService walletService, PaymentDatabaseController paymentDatabaseController) {
        this.walletService = walletService;
        this.paymentDatabaseController = paymentDatabaseController;
    }

    @PostMapping("/pay_using_wallet")
    public ResponseEntity<?> payUsingWallet(@RequestBody @Valid WalletPaymentRequest walletPaymentRequest) {
        Wallet wallet = walletService.payUsingWallet(walletPaymentRequest);

        RecordPaymentRequest recordPaymentRequest = RecordPaymentRequest.builder()
                .billId(walletPaymentRequest.getBillId())
                .amount(walletPaymentRequest.getAmount())
                .paymentMethod(walletPaymentRequest.getPaymentMethod())
                .paymentStatus(walletPaymentRequest.getPaymentStatus())
                .txnRefId(UUID.randomUUID().toString())
                .build();

        return paymentDatabaseController.recordPayment(recordPaymentRequest);
    }

    @GetMapping("/{customerId}/balance")
    public ResponseEntity<?> getWalletBalance(@PathVariable("customerId") String customerId) {
        Wallet wallet = walletService.getWalletBalance(customerId);
        return new ResponseEntity<>(new GenericResponse<>("Wallet Balance Retreived successfully ",wallet),
                HttpStatus.OK);
    }


}
