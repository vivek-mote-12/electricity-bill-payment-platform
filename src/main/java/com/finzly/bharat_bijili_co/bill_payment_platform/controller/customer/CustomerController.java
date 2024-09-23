package com.finzly.bharat_bijili_co.bill_payment_platform.controller.customer;

import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.CreateCustomerRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.UpdateCustomerRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.response.CsvResponse;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.response.GenericResponse;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.CustomerNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.InvalidFileFormatException;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.finzly.bharat_bijili_co.bill_payment_platform.service.customer.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CreateCustomerService createCustomerService;
    private final GetCustomersService getCustomersService;
    private final UpdateCustomerService updateCustomerService;
    private final FilterCustomerService filterCustomerService;
    private final DeleteCustomerService deleteCustomerService;
    private final AddCustomersUsingCsvService addCustomersUsingCsvService;


    public CustomerController(
            CreateCustomerService createCustomerService,
            GetCustomersService getCustomersService,
            UpdateCustomerService updateCustomerService,
            FilterCustomerService filterCustomerService,
            DeleteCustomerService deleteCustomerService,
            AddCustomersUsingCsvService addCustomersUsingCsvService
    ){
        this.createCustomerService=createCustomerService;
        this.getCustomersService=getCustomersService;
        this.updateCustomerService=updateCustomerService;
        this.filterCustomerService=filterCustomerService;
        this.deleteCustomerService=deleteCustomerService;
        this.addCustomersUsingCsvService=addCustomersUsingCsvService;
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody @Valid CreateCustomerRequest createCustomerRequest) {
        Customer createdCustomer = createCustomerService.createCustomer(createCustomerRequest);

        return new ResponseEntity<>(new GenericResponse<>("Created customer successfully",createdCustomer)
                , HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllCustomers(){
        List<Customer> customers=getCustomersService.getAllCustomers();

        return new ResponseEntity<>(new GenericResponse<>("All customers",customers),HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable("customerId")String customerId){
        Customer customer=getCustomersService.getCustomerById(customerId);
        return new ResponseEntity<>(new GenericResponse<>("Success",customer),HttpStatus.OK);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<?> updateCustomer(
            @PathVariable("customerId") String customerId,
            @RequestBody @Valid UpdateCustomerRequest updateCustomerRequest
    ){
        Customer updatedCustomer=updateCustomerService.updateCustomer(customerId,updateCustomerRequest);

        return new ResponseEntity<>(new GenericResponse<>("Updated customer successfully",updatedCustomer),
                HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterCustomersByCity(@RequestParam String city) {
        List<Customer> customers = filterCustomerService.filterCustomersByCity(city);
        return new ResponseEntity<>(new GenericResponse<>("Success",customers),HttpStatus.OK);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable String customerId) {
        deleteCustomerService.deleteCustomer(customerId);
        return new ResponseEntity<>(new GenericResponse<>("Customer deleted successfully.",null)
                ,HttpStatus.OK);
    }

    @PostMapping("/csv")
    public ResponseEntity<?> addCustomersFromCsv(@RequestParam("file") MultipartFile file) {
        CsvResponse response = addCustomersUsingCsvService.addCustomersFromCsv(file);
        return new ResponseEntity<>(new GenericResponse<>("Records Added",response),HttpStatus.CREATED);
    }
}
