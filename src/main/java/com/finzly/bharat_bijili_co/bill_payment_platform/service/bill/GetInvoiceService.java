package com.finzly.bharat_bijili_co.bill_payment_platform.service.bill;

import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.GetInvoiceDetailsRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.response.GetInvoiceResponse;
import com.finzly.bharat_bijili_co.bill_payment_platform.enums.PaymentMethod;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.BillAlreadyMarkedAsPaidException;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.BillNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Bill;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.bill.BillRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;

@Service
public class GetInvoiceService {
    private BillRepository billRepository;

    public GetInvoiceService(BillRepository billRepository){
        this.billRepository=billRepository;
    }

    public GetInvoiceResponse getInvoiceDetails(String billId,GetInvoiceDetailsRequest getInvoiceDetailsRequest){
        Bill bill=billRepository.findByBillId(billId).orElseThrow(()->new BillNotFoundException("Bill not found"));

        if(bill.getIsPaid()){
            throw new BillAlreadyMarkedAsPaidException("Bill already marked as paid");
        }

        Double discountAmount=0.0;
        Boolean isDiscountApplied=false;

        LocalDate dueDate = bill.getDueDate().toLocalDate();
        if (LocalDate.now().isBefore(dueDate)) {
            discountAmount += bill.getAmountDue() * 0.05;
        }

        if(!(getInvoiceDetailsRequest.getPaymentMethod()== PaymentMethod.CASH)){
            discountAmount += bill.getAmountDue() * 0.05;
        }

        Double totalAmountAfterDiscount=bill.getAmountDue()-discountAmount;

        if(discountAmount>0.0){
            isDiscountApplied=true;
        }

        GetInvoiceResponse getInvoiceResponse=GetInvoiceResponse
                .builder()
                .bill(bill)
                .isDiscountApplied(isDiscountApplied)
                .discountAmount(discountAmount)
                .totalAmountAfterDiscount(totalAmountAfterDiscount)
                .build();

        return getInvoiceResponse;
    }
}
