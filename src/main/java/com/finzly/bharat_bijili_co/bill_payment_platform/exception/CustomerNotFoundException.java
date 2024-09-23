package com.finzly.bharat_bijili_co.bill_payment_platform.exception;

public class CustomerNotFoundException extends RuntimeException{
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
