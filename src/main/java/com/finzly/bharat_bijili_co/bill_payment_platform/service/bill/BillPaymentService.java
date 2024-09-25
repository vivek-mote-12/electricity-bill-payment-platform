package com.finzly.bharat_bijili_co.bill_payment_platform.service.bill;

import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.MarkBillAsPaidRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.BillAlreadyMarkedAsPaidException;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.BillNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.PaymentNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Bill;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Payment;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.bill.BillRepository;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.payment.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class BillPaymentService {
    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;

    public  BillPaymentService(BillRepository billRepository,PaymentRepository paymentRepository){
        this.billRepository=billRepository;
        this.paymentRepository=paymentRepository;
    }

    public Bill markBillAsPaid(String billId, MarkBillAsPaidRequest markBillAsPaidRequest){
        Bill bill=billRepository.findByBillId(billId).orElseThrow(()->new BillNotFoundException("Bill not found"));

        Payment payment=paymentRepository.findByPaymentId(markBillAsPaidRequest.getPaymentId()).orElseThrow(()
                ->new PaymentNotFoundException("Payment not found with given ID"));

        if(bill.getIsPaid()){
            throw new BillAlreadyMarkedAsPaidException("Bill already marked as paid");
        }

        bill.setIsPaid(true);
        bill.setPaymentId(markBillAsPaidRequest.getPaymentId());
        bill.setDiscountAmount(markBillAsPaidRequest.getDiscountAmount());
        bill.setIsDiscountApplied(markBillAsPaidRequest.getIsDiscountApplied());

        return billRepository.save(bill);
    }
}
