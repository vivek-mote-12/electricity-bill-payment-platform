package com.finzly.bharat_bijili_co.bill_payment_platform.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Date;

@Data
public class CreateBillRequest {
    @NotBlank(message = "CustomerId is mandatory")
    private String customerId;

    @NotNull(message = "Current meter reading is required")
    private Long currentMeterReading;

    @NotNull(message = "End date is required")
    private Date endDate;
}
