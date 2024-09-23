package com.finzly.bharat_bijili_co.bill_payment_platform.exception;

public class CustomerAlreadyExistsException extends RuntimeException{
    public CustomerAlreadyExistsException(String message){
        super(message);
    }
}
