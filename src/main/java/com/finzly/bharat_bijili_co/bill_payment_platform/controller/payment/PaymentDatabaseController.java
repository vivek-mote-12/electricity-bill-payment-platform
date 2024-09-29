package com.finzly.bharat_bijili_co.bill_payment_platform.controller.payment;

import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.RecordPaymentRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.response.GenericResponse;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Payment;
import com.finzly.bharat_bijili_co.bill_payment_platform.service.payment.GenerateReceiptService;
import com.finzly.bharat_bijili_co.bill_payment_platform.service.payment.GetPaymentService;
import com.finzly.bharat_bijili_co.bill_payment_platform.service.payment.RecordPaymentService;
import com.itextpdf.text.DocumentException;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentDatabaseController {
    private final RecordPaymentService recordPaymentService;
    private GetPaymentService getPaymentService;
    private GenerateReceiptService generateReceiptService;

    public PaymentDatabaseController(RecordPaymentService recordPaymentService,
                                     GetPaymentService getPaymentService,
                                     GenerateReceiptService generateReceiptService){
        this.recordPaymentService=recordPaymentService;
        this.getPaymentService=getPaymentService;
        this.generateReceiptService=generateReceiptService;
    }

    @PostMapping("/record")
    public ResponseEntity<?> recordPayment(@RequestBody @Valid RecordPaymentRequest recordPaymentRequest){
        Payment payment=recordPaymentService.recordPayment(recordPaymentRequest);

        return new ResponseEntity<>(new GenericResponse<>("Payment record added successfully",payment), HttpStatus.OK);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getPaymentsForById(@PathVariable("paymentId")String paymentId){
        Payment payment=getPaymentService.getPaymentById(paymentId);
        return new ResponseEntity<>(new GenericResponse<>("Success",payment),HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getPaymentsForCustomer(@PathVariable("customerId")String customerId){
        List<Payment> payments=getPaymentService.getPaymentsByCustomerId(customerId);
        return new ResponseEntity<>(new GenericResponse<>("Success",payments),HttpStatus.OK);
    }

    @GetMapping("/generateReceipt/{paymentId}")
    public ResponseEntity<byte[]> generateReceipt(@PathVariable("paymentId") String paymentId) throws DocumentException, IOException {
        ByteArrayInputStream receipt = generateReceiptService.generateReceipt(paymentId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=receipt.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(receipt.readAllBytes());
    }
}

























