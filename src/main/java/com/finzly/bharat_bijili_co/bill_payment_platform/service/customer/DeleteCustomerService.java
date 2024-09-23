package com.finzly.bharat_bijili_co.bill_payment_platform.service.customer;

import com.finzly.bharat_bijili_co.bill_payment_platform.exception.CustomerNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.customer.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DeleteCustomerService {
    private final CustomerRepository customerRepository;

    public DeleteCustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public void deleteCustomer(String customerId) {
        if (!customerRepository.existsByCustomerId(customerId)) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found.");
        }
        customerRepository.deleteByCustomerId(customerId);
    }
}
