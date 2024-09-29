package com.finzly.bharat_bijili_co.bill_payment_platform.exception;

public class InsufficientWalletBalanceException extends RuntimeException {
    public InsufficientWalletBalanceException(String message) {
        super(message);
    }
}
