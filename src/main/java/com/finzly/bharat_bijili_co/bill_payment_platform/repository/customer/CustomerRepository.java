package com.finzly.bharat_bijili_co.bill_payment_platform.repository.customer;

import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Integer>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByCustomerId(String customerId);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByCustomerId(String customerId);

    List<Customer> findByCity(String city);
    Optional<Customer> findByPhone(String phone);
    Optional<Customer> findByEmail(String phone);



    //    @Query("SELECT c.email FROM Customer c WHERE c.email = :email")
    @Query("SELECT c.email FROM Customer c WHERE c.customerId = :customerId")
    String findEmailByCustomerId(String customerId);

    //   String findCustomerEmailByCustomerId(String customerId);


    void deleteByCustomerId(String customerId);

    @Query("SELECT DISTINCT LOWER(c.city) FROM Customer c")
    List<String> findDistinctCities();

    @Query("SELECT COUNT(c) FROM Customer c")
    Long getTotalCustomers();
}
