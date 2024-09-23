package com.finzly.bharat_bijili_co.bill_payment_platform.service.customer;

import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.UpdateCustomerRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.CustomerNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.customer.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateCustomerService {
    private final CustomerRepository customerRepository;

    public UpdateCustomerService(CustomerRepository customerRepository){
        this.customerRepository=customerRepository;
    }

    public Customer updateCustomer(String customerId, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with given ID"));

        // Update fields based on request
        if (request.getName() != null) {
            customer.setName(request.getName());
        }
        if (request.getEmail() != null) {
            customer.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            customer.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            customer.setAddress(request.getAddress());
        }
        if (request.getCity() != null) {
            customer.setCity(request.getCity());
        }
        if (request.getMeterReading() != null) {
            customer.setMeterReading(request.getMeterReading());
        }
        if (request.getPreviousBillDate() != null) {
            customer.setPreviousBillDate(request.getPreviousBillDate());
        }

        return customerRepository.save(customer);
    }
}
