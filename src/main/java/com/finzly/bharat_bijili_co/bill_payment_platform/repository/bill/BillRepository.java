package com.finzly.bharat_bijili_co.bill_payment_platform.repository.bill;

import com.finzly.bharat_bijili_co.bill_payment_platform.model.Bill;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill,Integer> {
    Optional<Bill> findByBillId(String billId);
    List<Bill> findByCustomerOrderByIsPaidAscDueDateDesc(Customer customer);
}