package com.finzly.bharat_bijili_co.bill_payment_platform.service.customerCardDetails;

import com.finzly.bharat_bijili_co.bill_payment_platform.exception.CustomerCardDetailsNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.customerCardDetails.CustomerCardDetailsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DeleteCardDetailsService {
    private final CustomerCardDetailsRepository customerCardDetailsRepository;

    public DeleteCardDetailsService(CustomerCardDetailsRepository customerCardDetailsRepository) {
        this.customerCardDetailsRepository = customerCardDetailsRepository;
    }

    @Transactional
    public void deleteCustomerCardDetailsByCustomer(Customer customer) {
        if(!customerCardDetailsRepository.existsByCustomer(customer)) {
            throw new CustomerCardDetailsNotFoundException("card with Customer id " + customer.getCustomerId() + " not found");
        }
        customerCardDetailsRepository.deleteByCustomer(customer);
    }
}
