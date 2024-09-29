package com.finzly.bharat_bijili_co.bill_payment_platform.exception;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(String message) {
        super(message);
    }
}
