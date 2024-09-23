package com.finzly.bharat_bijili_co.bill_payment_platform.service.customer;

import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.customer.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilterCustomerService {
    private final CustomerRepository customerRepository;

    public FilterCustomerService(CustomerRepository customerRepository){
        this.customerRepository=customerRepository;
    }

    public List<Customer> filterCustomersByCity(String city) {
        return customerRepository.findByCity(city);
    }
}
