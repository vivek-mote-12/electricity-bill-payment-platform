package com.finzly.bharat_bijili_co.bill_payment_platform.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {
    private long totalCustomers;
    private long totalUnitsConsumed;
    private long totalPendingBills;
    private double totalAmountPending;
}
