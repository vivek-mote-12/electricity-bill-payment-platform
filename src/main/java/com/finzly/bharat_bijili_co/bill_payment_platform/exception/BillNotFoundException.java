package com.finzly.bharat_bijili_co.bill_payment_platform.exception;

public class BillNotFoundException extends RuntimeException{
    public BillNotFoundException(String message) {
        super(message);
    }
}
