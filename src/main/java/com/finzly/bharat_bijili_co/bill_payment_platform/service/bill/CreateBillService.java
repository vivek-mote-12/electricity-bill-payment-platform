package com.finzly.bharat_bijili_co.bill_payment_platform.service.bill;

import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.CreateBillRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.CustomerNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.ValidationException;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Bill;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.bill.BillRepository;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class CreateBillService {
    private final BillRepository billRepository;
    private final CustomerRepository customerRepository;

    @Value("${bill.billing.rate}")
    private Double billingRate;

    @Value("${bill.due.date.after}")
    private Integer dueDateAfter;

    public CreateBillService(BillRepository billRepository,CustomerRepository customerRepository){
        this.billRepository=billRepository;
        this.customerRepository=customerRepository;
    }

    public Bill generateBill(CreateBillRequest createBillRequest){
        Customer customer = customerRepository.findByCustomerId(createBillRequest.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " +
                        createBillRequest.getCustomerId()));

        Date startDate=customer.getPreviousBillDate();

        if(createBillRequest.getCurrentMeterReading()<customer.getMeterReading()){
            throw new ValidationException("Meter reading is not valid");
        }
        Long unitsConsumed=createBillRequest.getCurrentMeterReading()-customer.getMeterReading();

        Double amountDue=unitsConsumed*billingRate;

        // Automatically set the dueDate to 15 days after endDate
        LocalDate endLocalDate = createBillRequest.getEndDate().toLocalDate();
        Date dueDate = Date.valueOf(endLocalDate.plus(dueDateAfter, ChronoUnit.DAYS));

        Bill bill=Bill
                .builder()
                .customer(customer)
                .unitsConsumed(unitsConsumed)
                .startDate(startDate)
                .endDate(createBillRequest.getEndDate())
                .dueDate(dueDate)
                .amountDue(amountDue)
                .build();

        Bill savedBill=billRepository.save(bill);

        customer.setMeterReading(createBillRequest.getCurrentMeterReading());
        customer.setPreviousBillDate(createBillRequest.getEndDate());

        customerRepository.save(customer);

        return savedBill;
    }
}
