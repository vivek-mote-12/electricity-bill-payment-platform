package com.finzly.bharat_bijili_co.bill_payment_platform.repository.customer;

import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    Optional<Customer> findByCustomerId(String customerId);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByCustomerId(String customerId);

    List<Customer> findByCity(String city);
    Optional<Customer> findByPhone(String phone);
    Optional<Customer> findByEmail(String phone);


    void deleteByCustomerId(String customerId);
}
