package com.finzly.bharat_bijili_co.bill_payment_platform.exception;

public class BillAlreadyMarkedAsPaidException extends RuntimeException{
    public BillAlreadyMarkedAsPaidException(String message){
        super(message);
    }
}
