package com.finzly.bharat_bijili_co.bill_payment_platform.controller.customerCardDetails;

import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.CreateCustomerCardDetailsRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.response.GenericResponse;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.CustomerCardDetails;
import com.finzly.bharat_bijili_co.bill_payment_platform.service.customer.GetCustomersService;
import com.finzly.bharat_bijili_co.bill_payment_platform.service.customerCardDetails.CreateCardDetailsService;
import com.finzly.bharat_bijili_co.bill_payment_platform.service.customerCardDetails.DeleteCardDetailsService;
import com.finzly.bharat_bijili_co.bill_payment_platform.service.customerCardDetails.GetCardDetailsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer_card_details")
public class CustomerCardDetailsController {
    private final CreateCardDetailsService createCardDetailsService;
    private final DeleteCardDetailsService deleteCustomerCardDetailsService;
    private final GetCardDetailsService getCardDetailsService;
    private final GetCustomersService getCustomersService;

    public CustomerCardDetailsController(
            CreateCardDetailsService createCardDetailsService,
            DeleteCardDetailsService deleteCardDetailsService,
            GetCardDetailsService getCardDetailsService,
            GetCustomersService getCustomersService
    ){
        this.createCardDetailsService = createCardDetailsService;
        this.deleteCustomerCardDetailsService = deleteCardDetailsService;
        this.getCardDetailsService = getCardDetailsService;
        this.getCustomersService = getCustomersService;
    }

    @PostMapping
    public ResponseEntity<?> createCustomerCardDetails(@RequestBody @Valid CreateCustomerCardDetailsRequest createCustomerCardDetailsRequest) {
        CustomerCardDetails customerCardDetails = createCardDetailsService.createCustomerCardDetails(createCustomerCardDetailsRequest);
        return new ResponseEntity<>(new GenericResponse<>("Created Card details successfully",customerCardDetails),
                HttpStatus.CREATED);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerCardDetails(@PathVariable("customerId") String customerId) {
        Customer customer = getCustomersService.getCustomerById(customerId);
        List<CustomerCardDetails> cards = getCardDetailsService.getCustomerCardDetailsByCustomer(customer);
        return new ResponseEntity<>(new GenericResponse<>("Success",cards),
                HttpStatus.OK);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> deleteCustomerCardDetails(@PathVariable("customerId") String customerId) {
        Customer customer = getCustomersService.getCustomerById(customerId);
        deleteCustomerCardDetailsService.deleteCustomerCardDetailsByCustomer(customer);
        return new ResponseEntity<>(new GenericResponse<>("Deleted successfully",null),
                HttpStatus.OK);
    }

}
