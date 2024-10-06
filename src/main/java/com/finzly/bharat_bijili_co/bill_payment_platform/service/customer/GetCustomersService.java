package com.finzly.bharat_bijili_co.bill_payment_platform.service.customer;

import com.finzly.bharat_bijili_co.bill_payment_platform.exception.CustomerNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.customer.CustomerRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetCustomersService {
    CustomerRepository customerRepository;

    public GetCustomersService(CustomerRepository customerRepository){
        this.customerRepository=customerRepository;
    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    public Customer getCustomerById(String customerId){
        return customerRepository.findByCustomerId(customerId).orElseThrow(()->
                new CustomerNotFoundException("Customer with ID "+customerId+" does not exists")
        );
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email).orElseThrow(()->
                new CustomerNotFoundException("Customer with email "+email+" does not exists")
        );
    }

    public Page<Customer> getCustomers(String search, String city, Pageable pageable) {
        Specification<Customer> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isEmpty()) {
                String searchLower = "%" + search.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchLower),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchLower),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), searchLower),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("customerId")), searchLower)
                ));
            }

            if (city != null && !city.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("city"), city));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return customerRepository.findAll(spec, pageable);
    }

    public List<String> getDistinctCities(){
        return customerRepository.findDistinctCities();
    }
}
