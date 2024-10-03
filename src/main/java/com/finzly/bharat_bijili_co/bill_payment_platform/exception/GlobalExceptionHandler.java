package com.finzly.bharat_bijili_co.bill_payment_platform.exception;

import com.finzly.bharat_bijili_co.bill_payment_platform.dto.response.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();

        return new ResponseEntity<>(new GenericResponse<>(errorMessage, null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<Object> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(new GenericResponse<>(ex.getMessage(),body), HttpStatus.CONFLICT);
    }
    // Handle CustomerNotFoundException
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Object> handleCustomerNotFoundException(CustomerNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(new GenericResponse<>(ex.getMessage(),body), HttpStatus.NOT_FOUND);
    }

    // Handle ValidationException
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<GenericResponse<?>> handleValidationException(ValidationException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(new GenericResponse<Map<String,Object>>(ex.getMessage(), body), HttpStatus.BAD_REQUEST);
    }
    //Handle invalid file format exception
    @ExceptionHandler(InvalidFileFormatException.class)
    public ResponseEntity<GenericResponse<?>> handleInvalidFileFormatException(InvalidFileFormatException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(new GenericResponse<Map<String,Object>>(ex.getMessage(), body), HttpStatus.BAD_REQUEST);
    }

    //Bill not found exception
    @ExceptionHandler(BillNotFoundException.class)
    public ResponseEntity<GenericResponse<?>> handleBillNotFoundException(BillNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(new GenericResponse<Map<String,Object>>(ex.getMessage(), body), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BillAlreadyMarkedAsPaidException.class)
    public ResponseEntity<GenericResponse<?>> handleBillAlreadyMarkedAsPaidException(BillAlreadyMarkedAsPaidException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(new GenericResponse<Map<String,Object>>(ex.getMessage(), body), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<GenericResponse<?>> handlePaymentNotFoundException(PaymentNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(new GenericResponse<Map<String,Object>>(ex.getMessage(), body), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BillCannotBeDeletedException.class)
    public ResponseEntity<GenericResponse<?>> handleBillCannotBeDeleteException(BillCannotBeDeletedException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(new GenericResponse<Map<String,Object>>(ex.getMessage(), body), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvoiceCannotBeGeneratedException.class)
    public ResponseEntity<GenericResponse<?>> handleInvoiceCannotBeGeneratedException(InvoiceCannotBeGeneratedException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(new GenericResponse<Map<String,Object>>(ex.getMessage(), body), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FailedToSendEmailException.class)
    public ResponseEntity<GenericResponse<?>> handleFailedToSendEmailException(FailedToSendEmailException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(new GenericResponse<Map<String,Object>>(ex.getMessage(), body), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidOrExpiredOtpException.class)
    public ResponseEntity<GenericResponse<?>> handleInvalidOrExpiredException(InvalidOrExpiredOtpException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(new GenericResponse<Map<String,Object>>(ex.getMessage(), body), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReceiptCannotBeGenerated.class)
    public ResponseEntity<GenericResponse<?>> handleReceiptCannotBeGenerateException(ReceiptCannotBeGenerated ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(new GenericResponse<Map<String,Object>>(ex.getMessage(), body), HttpStatus.BAD_REQUEST);
    }



    //Not able to send OTP on Email
    @ExceptionHandler(NotAbleToSendOtpException.class)
    public ResponseEntity<GenericResponse<?>> handleNotAbleToSendOtpException(NotAbleToSendOtpException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(new GenericResponse<Map<String,Object>>(ex.getMessage(), body), HttpStatus.BAD_REQUEST);
    }

    //Customer Card Details Already Exists
    @ExceptionHandler(CustomerCardDetailsAlreadyExistsException.class)
    public ResponseEntity<GenericResponse<?>> handleCustomerCardDetailsAlreadyExistsException(CustomerCardDetailsAlreadyExistsException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(new GenericResponse<Map<String,Object>>(ex.getMessage(), body), HttpStatus.OK);
    }

    //Customer Card Details Not Found
    @ExceptionHandler(CustomerCardDetailsNotFoundException.class)
    public ResponseEntity<GenericResponse<?>> handleCustomerCardDetailsNotFoundException(CustomerCardDetailsNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(new GenericResponse<Map<String,Object>>(ex.getMessage(), body), HttpStatus.BAD_REQUEST);
    }

    // Wallet not found for custId
    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<GenericResponse<?>> handleWalletNotFoundException(WalletNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(new GenericResponse<Map<String,Object>>(ex.getMessage(), body), HttpStatus.BAD_REQUEST);
    }


    // Wallet balance is not sufficient to pay bill
    @ExceptionHandler(InsufficientWalletBalanceException.class)
    public ResponseEntity<GenericResponse<?>> handleInsufficientWalletBalanceException(InsufficientWalletBalanceException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(new GenericResponse<Map<String,Object>>(ex.getMessage(), body), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GenericResponse<?>> handleRuntimeExceptions(RuntimeException ex) {
        System.out.println(ex.toString());
        return new ResponseEntity<>(new GenericResponse<>(ex.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "An unexpected error occurred.");

        return new ResponseEntity<>(new GenericResponse<>(ex.getMessage(),body), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


















