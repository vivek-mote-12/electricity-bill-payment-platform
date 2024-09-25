package com.finzly.bharat_bijili_co.bill_payment_platform.service.bill;

import com.finzly.bharat_bijili_co.bill_payment_platform.enums.PaymentStatus;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.BillCannotBeDeletedException;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.BillNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Bill;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.bill.BillRepository;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.payment.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DeleteBillService {
    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;

    public DeleteBillService(PaymentRepository paymentRepository,BillRepository billRepository){
        this.paymentRepository=paymentRepository;
        this.billRepository=billRepository;
    }

    @Transactional
    public void deleteBill(String billId){
        Bill bill=billRepository.findByBillId(billId).orElseThrow(()->new BillNotFoundException("Bill not found with given Id"));

        // Check if the bill is already paid
        if(bill.getIsPaid()){
            throw new BillCannotBeDeletedException("Paid bills cannot be deleted");
        }

        // Check for any failed payment entries for the bill and delete them
        paymentRepository.deleteByBillAndPaymentStatus(bill, PaymentStatus.FAILED);

        billRepository.delete(bill);
    }
}
