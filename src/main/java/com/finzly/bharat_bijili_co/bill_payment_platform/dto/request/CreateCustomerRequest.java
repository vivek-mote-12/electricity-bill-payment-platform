package com.finzly.bharat_bijili_co.bill_payment_platform.dto.request;

import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class CreateCustomerRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits")
    private String phone;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotNull(message = "Meter reading is required")
    private Long meterReading;

    @NotNull(message = "Previous bill date is required")
    private Date previousBillDate;

    public Customer toCustomer() {
        return Customer.builder()
                .name(this.getName())
                .email(this.getEmail())
                .phone(this.getPhone())
                .address(this.getAddress())
                .city(this.getCity())
                .meterReading(this.getMeterReading())
                .previousBillDate(this.getPreviousBillDate())
                .build();
    }
}
