package com.finzly.bharat_bijili_co.bill_payment_platform.exception;

public class FailedToSendEmailException extends RuntimeException{
    public FailedToSendEmailException(String message){
        super(message);
    }
}
