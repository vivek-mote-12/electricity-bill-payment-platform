package com.finzly.bharat_bijili_co.bill_payment_platform.repository.wallet;

import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WalletRepository extends JpaRepository <Wallet, Integer> {
    Optional<Wallet> findByCustomer(Customer customer);

    @Query("SELECT w FROM Wallet w WHERE w.customer.customerId = :customerId")
    Optional<Wallet> findByCustomerId(String customerId);

}
