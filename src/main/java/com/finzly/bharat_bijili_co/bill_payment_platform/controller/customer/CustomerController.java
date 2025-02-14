package com.finzly.bharat_bijili_co.bill_payment_platform.controller.customer;

import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.CreateCustomerRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.UpdateCustomerRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.response.CsvResponse;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.response.DashboardStatsResponse;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.response.GenericResponse;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.finzly.bharat_bijili_co.bill_payment_platform.service.customer.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final GetDashboardStatsService getDashboardStatsService;


    public CustomerController(
            CreateCustomerService createCustomerService,
            GetCustomersService getCustomersService,
            UpdateCustomerService updateCustomerService,
            FilterCustomerService filterCustomerService,
            DeleteCustomerService deleteCustomerService,
            AddCustomersUsingCsvService addCustomersUsingCsvService,
            GetDashboardStatsService getDashboardStatsService
    ){
        this.createCustomerService=createCustomerService;
        this.getCustomersService=getCustomersService;
        this.updateCustomerService=updateCustomerService;
        this.filterCustomerService=filterCustomerService;
        this.deleteCustomerService=deleteCustomerService;
        this.addCustomersUsingCsvService=addCustomersUsingCsvService;
        this.getDashboardStatsService = getDashboardStatsService;
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody @Valid CreateCustomerRequest createCustomerRequest) {
        Customer createdCustomer = createCustomerService.createCustomer(createCustomerRequest);

        return new ResponseEntity<>(new GenericResponse<>("Created customer successfully",createdCustomer)
                , HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:4200")
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

    @GetMapping("/page")
    public ResponseEntity<?> getCustomers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String[] sort) {
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortObj = Sort.by(direction, sort[0]);

        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<Customer> customers=getCustomersService.getCustomers(search, city, pageable);
        return new ResponseEntity<>(new GenericResponse<>("Success",customers),HttpStatus.OK);
    }

    @GetMapping("/cities/all")
    public ResponseEntity<?> getAllCities(){
        List<String> cities=getCustomersService.getDistinctCities();
        return new ResponseEntity<>(new GenericResponse<>("Success",cities),HttpStatus.OK);
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<?> getDashboardStats(){
        DashboardStatsResponse dashboardStatsResponse=getDashboardStatsService.getDashboardStats();
        return new ResponseEntity<>(new GenericResponse<>("Success",dashboardStatsResponse),HttpStatus.OK);
    }
}
