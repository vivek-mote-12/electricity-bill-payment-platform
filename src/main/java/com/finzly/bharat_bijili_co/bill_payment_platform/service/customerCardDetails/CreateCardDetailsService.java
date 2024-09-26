package com.finzly.bharat_bijili_co.bill_payment_platform.service.customerCardDetails;

import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.CreateCustomerCardDetailsRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.CustomerCardDetailsAlreadyExistsException;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.CustomerNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.CustomerCardDetails;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.customer.CustomerRepository;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.customerCardDetails.CustomerCardDetailsRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateCardDetailsService {
    private final CustomerCardDetailsRepository customerCardDetailsRepository;
    private final CustomerRepository customerRepository;

    public CreateCardDetailsService(CustomerCardDetailsRepository customerCardDetailsRepository, CustomerRepository customerRepository) {
        this.customerCardDetailsRepository = customerCardDetailsRepository;
        this.customerRepository = customerRepository;
    }

    public CustomerCardDetails createCustomerCardDetails(CreateCustomerCardDetailsRequest request) {
        if(customerCardDetailsRepository.existsByCardToken(request.getCardToken())){
            throw new CustomerCardDetailsAlreadyExistsException("This card is already exist. ");
        }
        CustomerCardDetails customerCardDetails = request.toCustomerCardDetails();
        customerCardDetails.setCardId(UUID.randomUUID().toString());
        Customer customer = customerRepository.findByCustomerId(request.getCustomerId()).orElseThrow(() ->
                new CustomerNotFoundException("Customer not found with ID: " +  request.getCustomerId()));

        customerCardDetails.setCustomer(customer);
        return customerCardDetailsRepository.save(customerCardDetails);
    }
}
