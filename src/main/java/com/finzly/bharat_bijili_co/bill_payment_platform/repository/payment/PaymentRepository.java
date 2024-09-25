package com.finzly.bharat_bijili_co.bill_payment_platform.repository.payment;

import com.finzly.bharat_bijili_co.bill_payment_platform.enums.PaymentStatus;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Bill;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {
    Optional<Payment> findByPaymentId(String paymentId);

    // Custom query to delete failed payments for the given bill
    void deleteByBillAndPaymentStatus(Bill bill, PaymentStatus status);
}
