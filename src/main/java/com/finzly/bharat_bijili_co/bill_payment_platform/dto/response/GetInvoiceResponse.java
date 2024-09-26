package com.finzly.bharat_bijili_co.bill_payment_platform.dto.response;

import com.finzly.bharat_bijili_co.bill_payment_platform.model.Bill;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetInvoiceResponse {
    private Bill bill;
    private Boolean isDiscountApplied;
    private Double discountAmount;
    private Double totalAmountAfterDiscount;
}
