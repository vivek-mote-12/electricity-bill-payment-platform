package com.finzly.bharat_bijili_co.bill_payment_platform.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.sql.Date;

@Data
public class UpdateCustomerRequest {
    private String name;

    @Email(message = "Email format is not valid")
    @NotBlank(message = "Email should be present")
    private String email;

    @NotBlank(message = "Mobile number cannot be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone;

    private String address;

    private String city;

    @NotNull(message = "Meter reading is required")
    private Long meterReading;

    @NotNull(message = "Previous bill date is required")
    @Past(message = "Previous bill date must be in the past")
    private Date previousBillDate;
}
