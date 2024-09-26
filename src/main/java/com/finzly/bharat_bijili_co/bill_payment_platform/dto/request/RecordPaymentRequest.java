package com.finzly.bharat_bijili_co.bill_payment_platform.dto.request;

import com.finzly.bharat_bijili_co.bill_payment_platform.enums.PaymentMethod;
import com.finzly.bharat_bijili_co.bill_payment_platform.enums.PaymentStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordPaymentRequest {
    @NotBlank(message = "Bill Id is required")
    private String billId;

    @NotNull(message = "Amount is required")
    @Min(value = 0,message = "Value cannot be less than zero")
    private Double amount;

    @NotNull(message = "Payment method cannot be null")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Payment status cannot be null")
    private PaymentStatus paymentStatus;

    @NotBlank(message = "Txn ref Id is required")
    private String txnRefId;
}
