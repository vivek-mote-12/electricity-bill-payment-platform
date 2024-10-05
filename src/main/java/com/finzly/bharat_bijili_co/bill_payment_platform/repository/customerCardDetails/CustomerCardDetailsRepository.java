package com.finzly.bharat_bijili_co.bill_payment_platform.repository.customerCardDetails;

import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.CustomerCardDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerCardDetailsRepository extends JpaRepository<CustomerCardDetails, Integer> {
    Optional<CustomerCardDetails> findByCardId(String cardId);

    List<CustomerCardDetails> findByCustomer(Customer customer);

    Boolean existsByCardId(String cardId);
    Boolean existsByCardToken(String cardToken);
    Boolean existsByCustomer(Customer customer);
    void deleteByCustomer(Customer customer);
    void deleteByCardId(String cardId);

}
