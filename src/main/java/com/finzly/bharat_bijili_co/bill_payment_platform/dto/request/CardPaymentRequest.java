package com.finzly.bharat_bijili_co.bill_payment_platform.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardPaymentRequest extends PaymentSwitchOnMethodRequest {
    @NotBlank
    private String customerId;
    @NotNull(message = "card details must not null")
    private CreateCustomerCardDetailsRequest customerCardDetails;
    @NotNull
    private Double billAmount;

    public CardPaymentRequest(PaymentMethodRequest paymentMethodRequest) {
        this.customerId = paymentMethodRequest.getCustomerId();
        this.customerCardDetails = paymentMethodRequest.getCustomerCardDetails();
        this.billAmount = paymentMethodRequest.getAmount();
    }
}
