package com.finzly.bharat_bijili_co.bill_payment_platform.service.customerCardDetails;

import com.finzly.bharat_bijili_co.bill_payment_platform.exception.CustomerCardDetailsNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.CustomerCardDetails;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.customerCardDetails.CustomerCardDetailsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetCardDetailsService {
    CustomerCardDetailsRepository customerCardDetailsRepository;
    public GetCardDetailsService(CustomerCardDetailsRepository customerCardDetailsRepository) {
        this.customerCardDetailsRepository = customerCardDetailsRepository;
    }

    public CustomerCardDetails getCustomerCardDetailsById(String cardId) {
        return customerCardDetailsRepository.findByCardId(cardId).orElseThrow(()->
                new CustomerCardDetailsNotFoundException("Card with id "+cardId+" does not exists")
        );
    }

    public List<CustomerCardDetails> getCustomerCardDetailsByCustomer(Customer customer) {
        return customerCardDetailsRepository.findByCustomer(customer);
    }
}
