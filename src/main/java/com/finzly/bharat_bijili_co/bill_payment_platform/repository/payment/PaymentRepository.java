package com.finzly.bharat_bijili_co.bill_payment_platform.repository.payment;

import com.finzly.bharat_bijili_co.bill_payment_platform.enums.PaymentStatus;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Bill;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {
    Optional<Payment> findByPaymentId(String paymentId);

    void deleteByBillAndPaymentStatus(Bill bill, PaymentStatus status);

    @Query("SELECT p FROM Payment p WHERE p.bill.customer.customerId = :customerId")
    List<Payment> findPaymentsByCustomerId(@Param("customerId") String customerId);
}
