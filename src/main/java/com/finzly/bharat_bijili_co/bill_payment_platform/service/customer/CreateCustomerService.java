package com.finzly.bharat_bijili_co.bill_payment_platform.service.customer;

import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.CreateCustomerRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.CustomerAlreadyExistsException;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Wallet;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.customer.CustomerRepository;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.wallet.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateCustomerService {
    private final CustomerRepository customerRepository;
    private final WalletRepository walletRepository;

    public CreateCustomerService(CustomerRepository customerRepository, WalletRepository walletRepository){
        this.customerRepository=customerRepository;
        this.walletRepository = walletRepository;
    }

    public Customer createCustomer(CreateCustomerRequest createCustomerRequest) {
        // Check if customer with email or phone already exists
        if (customerRepository.existsByEmail(createCustomerRequest.getEmail())) {
            throw new CustomerAlreadyExistsException("Customer with the same email already exists.");
        }
        if (customerRepository.existsByPhone(createCustomerRequest.getPhone())) {
            throw new CustomerAlreadyExistsException("Customer with the same phone already exists.");
        }

        Customer customer=createCustomerRequest.toCustomer();
        // Generate a unique customer ID
        customer.setCustomerId(UUID.randomUUID().toString());
        customerRepository.save(customer);

        Wallet wallet = new Wallet();
        wallet.setCustomer(customer);
        walletRepository.save(wallet); // creation of wallet on creation of customer

        return customer;
    }
}
