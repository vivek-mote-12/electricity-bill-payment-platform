package com.finzly.bharat_bijili_co.bill_payment_platform.exception;

public class InvalidOrExpiredOtpException extends RuntimeException{
    public InvalidOrExpiredOtpException(String message){
        super(message);
    }
}
