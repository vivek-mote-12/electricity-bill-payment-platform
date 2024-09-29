package com.finzly.bharat_bijili_co.bill_payment_platform.dto.request;

import com.finzly.bharat_bijili_co.bill_payment_platform.annotations.EnumValidator;
import com.finzly.bharat_bijili_co.bill_payment_platform.enums.CustomerCardType;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.CustomerCardDetails;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCustomerCardDetailsRequest {


    @NotBlank(message = "Card Cannot be created without customer reference")
    private String customerId;


    @NotBlank(message = "CardType required")
    @EnumValidator(enumClass = CustomerCardType.class, message = "Invalid card type, must be 'CREDIT_CARD' or 'DEBIT_CARD'")
    private String cardType;

    private String cardNetwork;

    @NotBlank(message = "Last four digits are must")
    @Pattern(regexp = "\\d{4}", message = " cardLastFour is not containing 4 digits")
    private String cardLastFour;

    @NotBlank(message = "cardToken is blank")
    private String cardToken;

    @NotBlank(message = "cardHolderName is Blank")
    private String cardHolderName;

    @NotBlank(message = "enter expiry month (MM/YY)")
    private String expiryMonth;

    public CustomerCardDetails toCustomerCardDetails() {
        return CustomerCardDetails.builder()
                .cardType(this.cardType)
                .cardNetwork(this.cardNetwork)
                .cardLastFour(this.cardLastFour)
                .cardToken(this.cardToken)
                .cardHolderName(this.cardHolderName)
                .expiryMonth(this.expiryMonth)
                .build();
    }
}
