package com.finzly.bharat_bijili_co.bill_payment_platform.dto.request;

import com.finzly.bharat_bijili_co.bill_payment_platform.enums.PaymentMethod;
import com.finzly.bharat_bijili_co.bill_payment_platform.enums.PaymentStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WalletPaymentRequest extends PaymentSwitchOnMethodRequest {

    @NotBlank(message = "customerId is required")
    private String customerId;

    @NotNull(message = "amount is required")
    @Min(value = 0,message = "Value cannot be less than zero")
    private Double amount;

    @NotBlank(message = "Bill Id is required")
    private String billId;

    @NotNull(message = "Payment method cannot be null")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Payment status cannot be null")
    private PaymentStatus paymentStatus;

    @NotBlank(message = "Txn ref Id is required")
    private String txnRefId;

    // Constructor that accepts PaymentMethodRequest
    public WalletPaymentRequest(PaymentMethodRequest paymentMethodRequest) {
        this.customerId = paymentMethodRequest.getCustomerId();
        this.amount = paymentMethodRequest.getAmount();
        this.billId = paymentMethodRequest.getBillId();
        this.paymentMethod = paymentMethodRequest.getPaymentMethod();
        this.paymentStatus = paymentMethodRequest.getPaymentStatus();
        this.txnRefId = paymentMethodRequest.getTxnRefId();
    }
}
