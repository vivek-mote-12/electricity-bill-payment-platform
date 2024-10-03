package com.finzly.bharat_bijili_co.bill_payment_platform.service.customer;

import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.CreateCustomerRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.response.CsvResponse;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.InvalidFileFormatException;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.customer.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AddCustomersUsingCsvService {
    private final CustomerRepository customerRepository;
    private final CreateCustomerService createCustomerService;

    public AddCustomersUsingCsvService(CustomerRepository customerRepository,
                                       CreateCustomerService createCustomerService){
        this.customerRepository=customerRepository;
        this.createCustomerService=createCustomerService;
    }

    public CsvResponse addCustomersFromCsv(MultipartFile file) throws InvalidFileFormatException {
        if (!file.getOriginalFilename().endsWith(".csv")) {
            throw new InvalidFileFormatException("Invalid file format. Please upload a CSV file.");
        }

        List<String> failedRecords = new ArrayList<>();
        int successfulCount = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length != 7) {
                    failedRecords.add("Entry " + lineNumber + ": Invalid number of fields");
                    lineNumber++;
                    continue;
                }

                // Validation
                String name = fields[0].trim();
                String email = fields[1].trim();
                String phone = fields[2].trim();
                String address = fields[3].trim();
                String city = fields[4].trim();
                String meterReadingStr = fields[5].trim();
                String previousBillDateStr = fields[6].trim();

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || city.isEmpty()) {
                    failedRecords.add("Entry " + lineNumber + ": Required field is missing");
                    lineNumber++;
                    continue;
                }

                if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    failedRecords.add("Entry " + lineNumber + ": Invalid email format");
                    lineNumber++;
                    continue;
                }

                if (!phone.matches("^[0-9]{10}$")) {
                    failedRecords.add("Entry " + lineNumber + ": Invalid phone number format");
                    lineNumber++;
                    continue;
                }

                Long meterReading;
                try {
                    meterReading = Long.parseLong(meterReadingStr);
                } catch (NumberFormatException e) {
                    failedRecords.add("Entry " + lineNumber + ": Invalid meter reading");
                    lineNumber++;
                    continue;
                }

                Date previousBillDate;
                try {
                    previousBillDate = Date.valueOf(previousBillDateStr);
                } catch (IllegalArgumentException e) {
                    failedRecords.add("Entry " + lineNumber + ": Invalid date format for previous bill date");
                    lineNumber++;
                    continue;
                }

                // Check if a customer with the same phone number already exists
                Optional<Customer> existingCustomer = customerRepository.findByPhone(phone);
                if (existingCustomer.isPresent()) {
                    failedRecords.add("Entry " + lineNumber + ": Duplicate phone number: " + phone);
                    lineNumber++;
                    continue;
                }

                Optional<Customer> existingCustomer2 = customerRepository.findByEmail(email);
                if (existingCustomer2.isPresent()) {
                    failedRecords.add("Entry " + lineNumber + ": Duplicate email: " + email);
                    lineNumber++;
                    continue;
                }

                // Create and save the customer
                CreateCustomerRequest createCustomerRequest=CreateCustomerRequest.builder()
                        .name(name)
                        .email(email)
                        .phone(phone)
                        .address(address)
                        .city(city)
                        .meterReading(meterReading)
                        .previousBillDate(previousBillDate)
                        .build();

                Customer customer=createCustomerService.createCustomer(createCustomerRequest);

                successfulCount++;
                lineNumber++;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing the CSV file: " + e.getMessage());
        }

        return new CsvResponse("Customers added successfully", successfulCount,
                failedRecords.size(), failedRecords);
    }
}
