package com.finzly.bharat_bijili_co.bill_payment_platform.exception;

public class PaymentNotFoundException extends RuntimeException{
    public PaymentNotFoundException(String message){
        super(message);
    }
}
