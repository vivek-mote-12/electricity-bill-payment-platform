package com.finzly.bharat_bijili_co.bill_payment_platform.repository.bill;

import com.finzly.bharat_bijili_co.bill_payment_platform.model.Bill;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill,Integer> {
    Optional<Bill> findByBillId(String billId);
    List<Bill> findByCustomerOrderByIsPaidAscDueDateDesc(Customer customer);

    // Fetching pending bills (where isPaid = false) and sorting them by dueDate in descending order
    List<Bill> findByIsPaidFalseOrderByDueDateDesc();

    List<Bill> findByCustomerAndIsPaidFalseOrderByDueDateDesc(Customer customer);

    @Query("SELECT SUM(b.unitsConsumed) FROM Bill b")
    Long getTotalUnitsConsumed();

    @Query("SELECT COUNT(b) FROM Bill b WHERE b.isPaid = false")
    Long getTotalPendingBills();

    @Query("SELECT SUM(b.amountDue) FROM Bill b WHERE b.isPaid = false")
    Double getTotalAmountPending();
}