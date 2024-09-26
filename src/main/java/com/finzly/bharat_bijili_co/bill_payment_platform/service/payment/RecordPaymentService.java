package com.finzly.bharat_bijili_co.bill_payment_platform.service.payment;

import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.GetInvoiceDetailsRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.MarkBillAsPaidRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.RecordPaymentRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.response.GetInvoiceResponse;
import com.finzly.bharat_bijili_co.bill_payment_platform.enums.PaymentStatus;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.BillAlreadyMarkedAsPaidException;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.BillNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.CustomerNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Bill;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Payment;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.bill.BillRepository;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.customer.CustomerRepository;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.payment.PaymentRepository;
import com.finzly.bharat_bijili_co.bill_payment_platform.service.bill.BillPaymentService;
import com.finzly.bharat_bijili_co.bill_payment_platform.service.bill.GetInvoiceService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class RecordPaymentService {
    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;
    private final CustomerRepository customerRepository;
    private final GetInvoiceService getInvoiceService;
    private final BillPaymentService billPaymentService;

    public RecordPaymentService(PaymentRepository paymentRepository,
                                BillRepository billRepository,
                                CustomerRepository customerRepository,
                                GetInvoiceService getInvoiceService,
                                BillPaymentService billPaymentService){
        this.paymentRepository=paymentRepository;
        this.billRepository=billRepository;
        this.customerRepository=customerRepository;
        this.getInvoiceService=getInvoiceService;
        this.billPaymentService=billPaymentService;
    }

//    @Transactional
    public Payment recordPayment(RecordPaymentRequest recordPaymentRequest){
        Bill bill=billRepository.findByBillId(recordPaymentRequest.getBillId()).orElseThrow(()->
                new BillNotFoundException("Bill not found with given Id"));

        if(bill.getIsPaid()){
            throw new BillAlreadyMarkedAsPaidException("Bill had been already paid");
        }

        GetInvoiceDetailsRequest getInvoiceDetailsRequest=GetInvoiceDetailsRequest
                .builder()
                .paymentMethod(recordPaymentRequest.getPaymentMethod())
                .build();

        GetInvoiceResponse getInvoiceResponse=getInvoiceService.getInvoiceDetails(bill.getBillId(),getInvoiceDetailsRequest);

        Date todayDate=Date.valueOf(LocalDate.now());

        Payment payment=Payment
                .builder()
                .bill(bill)
                .amount(recordPaymentRequest.getAmount())
                .paymentDate(todayDate)
                .paymentMethod(recordPaymentRequest.getPaymentMethod())
                .paymentStatus(recordPaymentRequest.getPaymentStatus())
                .txnRefId(recordPaymentRequest.getTxnRefId())
                .build();

        Payment savedPayment=paymentRepository.save(payment);

        if(payment.getPaymentStatus()== PaymentStatus.SUCCESS){
            MarkBillAsPaidRequest markBillAsPaidRequest=MarkBillAsPaidRequest
                    .builder()
                    .paymentId(savedPayment.getPaymentId())
                    .isDiscountApplied(getInvoiceResponse.getIsDiscountApplied())
                    .discountAmount(getInvoiceResponse.getDiscountAmount())
                    .build();

            billPaymentService.markBillAsPaid(bill.getBillId(),markBillAsPaidRequest);
        }
        return payment;
    }
}
