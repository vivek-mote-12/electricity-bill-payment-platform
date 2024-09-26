package com.finzly.bharat_bijili_co.bill_payment_platform.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MarkBillAsPaidRequest {
    @NotNull(message = "Is discount applied cannot be null")
    private Boolean isDiscountApplied;

    @NotNull(message = "Discount amount cannot be null")
    private Double discountAmount;

    @NotBlank(message = "paymentId cannot be blank")
    private String paymentId;
}
