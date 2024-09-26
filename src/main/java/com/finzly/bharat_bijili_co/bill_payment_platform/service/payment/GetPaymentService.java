package com.finzly.bharat_bijili_co.bill_payment_platform.service.payment;

import com.finzly.bharat_bijili_co.bill_payment_platform.exception.PaymentNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Payment;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.payment.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetPaymentService {
    private final PaymentRepository paymentRepository;

    public GetPaymentService(PaymentRepository paymentRepository){
        this.paymentRepository=paymentRepository;
    }

    public Payment getPaymentById(String paymentId){
        return paymentRepository.findByPaymentId(paymentId).orElseThrow(()->new PaymentNotFoundException("Payment not found"));
    }

    public List<Payment> getPaymentsByCustomerId(String customerId){
        return paymentRepository.findPaymentsByCustomerId(customerId);
    }
}
