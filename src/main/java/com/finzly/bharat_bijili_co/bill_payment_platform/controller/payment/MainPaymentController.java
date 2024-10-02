package com.finzly.bharat_bijili_co.bill_payment_platform.controller.payment;

import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.CardPaymentRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.PaymentMethodRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.WalletPaymentRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.enums.PaymentMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment_method")
public class MainPaymentController {

    CardPaymentController cardPaymentController;
    WalletPaymentController walletPaymentController;
    CardPaymentRequest cardPaymentRequest;

    MainPaymentController(CardPaymentController cardPaymentController, WalletPaymentController walletPaymentController) {
        this.cardPaymentController = cardPaymentController;
        this.walletPaymentController = walletPaymentController;
    }

    @PostMapping("/{paymentMethod}")
    public ResponseEntity<?> makePayment(@PathVariable("paymentMethod") PaymentMethod paymentMethod,@RequestBody PaymentMethodRequest paymentMethodRequest) {

        switch (paymentMethod) {
            case PaymentMethod.DEBIT_CARD, PaymentMethod.CREDIT_CARD:

                CardPaymentRequest cardPaymentRequest = new CardPaymentRequest(paymentMethodRequest);

                if (cardPaymentRequest == null) {
                    return ResponseEntity.badRequest().body("Invalid card payment details.");
                }

                // Process card payment
                return cardPaymentController.initiatePayment(cardPaymentRequest);

//                if (paymentMethodRequest.getPaymentSwitchOnMethod() instanceof CardPaymentRequest) {
//                    cardPaymentRequest = (CardPaymentRequest) paymentMethodRequest.getPaymentSwitchOnMethod();
//                    cardPaymentController.initiatePayment(cardPaymentRequest);
//                } else {
//                    return ResponseEntity.badRequest().body("Invalid card payment details.");
//                }

//                cardPaymentRequest = (CardPaymentRequest) paymentMethodRequest.getPaymentSwitchOnMethod();
//
//                cardPaymentController.initiatePayment(cardPaymentRequest);


            case PaymentMethod.WALLET:

                WalletPaymentRequest walletPaymentRequest = new WalletPaymentRequest(paymentMethodRequest);

                if (walletPaymentRequest == null) {
                    return ResponseEntity.badRequest().body("Invalid wallet payment details.");
                }

                // Process wallet payment
                return walletPaymentController.payUsingWallet(walletPaymentRequest);

//                if (paymentMethodRequest.getPaymentSwitchOnMethod() instanceof WalletPaymentRequest) {
//                    WalletPaymentRequest walletPaymentRequest = (WalletPaymentRequest) paymentMethodRequest.getPaymentSwitchOnMethod();
//                    walletPaymentController.payUsingWallet(walletPaymentRequest);
//                } else {
//                    return ResponseEntity.badRequest().body("Invalid wallet payment details.");
//                }

                // Handle wallet payment
//                WalletPaymentRequest walletPaymentRequest = (WalletPaymentRequest) paymentMethodRequest.getPaymentSwitchOnMethod();
                // Process wallet payment
//                walletPaymentController.payUsingWallet(walletPaymentRequest);


            default:
                return ResponseEntity.badRequest().body("Unsupported payment method.");
        }
//        return ResponseEntity.ok("Payment processed successfully.");
    }
}
