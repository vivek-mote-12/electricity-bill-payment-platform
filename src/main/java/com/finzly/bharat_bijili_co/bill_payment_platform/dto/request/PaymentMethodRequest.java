package com.finzly.bharat_bijili_co.bill_payment_platform.dto.request;

import com.finzly.bharat_bijili_co.bill_payment_platform.enums.PaymentMethod;
import com.finzly.bharat_bijili_co.bill_payment_platform.enums.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodRequest {
    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @NotNull(message = "Amount is required")
    private Double amount;

    @NotBlank(message = "Bill ID is required")
    private String billId;

    @NotBlank(message = "Transaction reference ID is required")
    private String txnRefId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private CreateCustomerCardDetailsRequest customerCardDetails;

    @NotNull(message = "Payment status is required")
    private PaymentStatus paymentStatus;
}
