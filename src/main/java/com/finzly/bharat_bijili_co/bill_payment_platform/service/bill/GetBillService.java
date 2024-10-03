package com.finzly.bharat_bijili_co.bill_payment_platform.service.bill;

import com.finzly.bharat_bijili_co.bill_payment_platform.exception.BillNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.CustomerNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Bill;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.bill.BillRepository;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.customer.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetBillService {
    private final BillRepository billRepository;
    private final CustomerRepository customerRepository;

    public GetBillService(BillRepository billRepository,CustomerRepository customerRepository){
        this.billRepository=billRepository;
        this.customerRepository=customerRepository;
    }

    public List<Bill> getAllBillsForCustomer(String customerId){
        Customer customer=customerRepository.findByCustomerId(customerId).orElseThrow(()
                ->new CustomerNotFoundException("Customer not found with Id "+customerId));

        return billRepository.findByCustomerOrderByIsPaidAscDueDateDesc(customer);
    }

    public List<Bill> getPendingBillsForCustomer(String customerId) {
        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with Id " + customerId));

        // Fetch only unpaid bills
        return billRepository.findByCustomerAndIsPaidFalseOrderByDueDateDesc(customer);
    }

    public Bill getBillById(String billId){
        return billRepository.findByBillId(billId).orElseThrow(()->new BillNotFoundException("Bill not found"));
    }

    public List<Bill> getAllPendingBills() {
        return billRepository.findByIsPaidFalseOrderByDueDateDesc();
    }
}
