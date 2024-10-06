package com.finzly.bharat_bijili_co.bill_payment_platform.service.customer;

import com.finzly.bharat_bijili_co.bill_payment_platform.dto.response.DashboardStatsResponse;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.bill.BillRepository;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.customer.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetDashboardStatsService {
    private final CustomerRepository customerRepository;
    private final BillRepository billRepository;

    public GetDashboardStatsService(CustomerRepository customerRepository, BillRepository billRepository) {
        this.customerRepository = customerRepository;
        this.billRepository = billRepository;
    }

    public DashboardStatsResponse getDashboardStats() {

        long totalCustomers = Optional.ofNullable(customerRepository.getTotalCustomers()).orElse(0L);
        long totalUnitsConsumed = Optional.ofNullable(billRepository.getTotalUnitsConsumed()).orElse(0L);
        long totalPendingBills = Optional.ofNullable(billRepository.getTotalPendingBills()).orElse(0L);
        double totalAmountPending = Optional.ofNullable(billRepository.getTotalAmountPending()).orElse(0.0);


        return new DashboardStatsResponse(totalCustomers, totalUnitsConsumed, totalPendingBills, totalAmountPending);
    }
}
