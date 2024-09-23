package com.finzly.bharat_bijili_co.bill_payment_platform.service.customer;

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

    public AddCustomersUsingCsvService(CustomerRepository customerRepository){
        this.customerRepository=customerRepository;
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
                    failedRecords.add("Line " + lineNumber + ": Invalid number of fields");
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
                    failedRecords.add("Line " + lineNumber + ": Required field is missing");
                    lineNumber++;
                    continue;
                }

                if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    failedRecords.add("Line " + lineNumber + ": Invalid email format");
                    lineNumber++;
                    continue;
                }

                if (!phone.matches("^[0-9]{10}$")) {
                    failedRecords.add("Line " + lineNumber + ": Invalid phone number format");
                    lineNumber++;
                    continue;
                }

                Long meterReading;
                try {
                    meterReading = Long.parseLong(meterReadingStr);
                } catch (NumberFormatException e) {
                    failedRecords.add("Line " + lineNumber + ": Invalid meter reading");
                    lineNumber++;
                    continue;
                }

                Date previousBillDate;
                try {
                    previousBillDate = Date.valueOf(previousBillDateStr);
                } catch (IllegalArgumentException e) {
                    failedRecords.add("Line " + lineNumber + ": Invalid date format for previous bill date");
                    lineNumber++;
                    continue;
                }

                // Check if a customer with the same phone number already exists
                Optional<Customer> existingCustomer = customerRepository.findByPhone(phone);
                if (existingCustomer.isPresent()) {
                    failedRecords.add("Line " + lineNumber + ": Duplicate phone number: " + phone);
                    lineNumber++;
                    continue;
                }

                existingCustomer = customerRepository.findByEmail(phone);
                if (existingCustomer.isPresent()) {
                    failedRecords.add("Line " + lineNumber + ": Duplicate email: " + email);
                    lineNumber++;
                    continue;
                }

                // Create and save the customer
                Customer customer = new Customer();
                customer.setCustomerId(UUID.randomUUID().toString());
                customer.setName(name);
                customer.setEmail(email);
                customer.setPhone(phone);
                customer.setAddress(address);
                customer.setCity(city);
                customer.setMeterReading(meterReading);
                customer.setPreviousBillDate(previousBillDate);

                customerRepository.save(customer);
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
