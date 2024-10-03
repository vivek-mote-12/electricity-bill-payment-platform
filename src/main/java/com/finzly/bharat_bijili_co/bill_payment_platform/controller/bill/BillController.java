package com.finzly.bharat_bijili_co.bill_payment_platform.controller.bill;

import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.CreateBillRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.GetInvoiceDetailsRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.MarkBillAsPaidRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.response.GenericResponse;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.response.GetInvoiceResponse;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Bill;
import com.finzly.bharat_bijili_co.bill_payment_platform.service.bill.*;
import com.itextpdf.text.DocumentException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {
    private final CreateBillService createBillService;
    private final GetBillService getBillService;
    private final BillPaymentService billPaymentService;
    private final DeleteBillService deleteBillService;
    private final InvoiceGenerationService invoiceGenerationService;
    private final GetInvoiceService getInvoiceService;
    private final SendInvoiceMailService sendInvoiceMailService;

    public BillController(CreateBillService createBillService,
                          GetBillService getBillService,
                          BillPaymentService billPaymentService,
                          DeleteBillService deleteBillService,
                          InvoiceGenerationService invoiceGenerationService,
                          GetInvoiceService getInvoiceService,
                          SendInvoiceMailService sendInvoiceMailService){
        this.createBillService=createBillService;
        this.getBillService=getBillService;
        this.billPaymentService=billPaymentService;
        this.deleteBillService=deleteBillService;
        this.invoiceGenerationService=invoiceGenerationService;
        this.getInvoiceService=getInvoiceService;
        this.sendInvoiceMailService=sendInvoiceMailService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateBill(@RequestBody @Valid CreateBillRequest createBillRequest){
        Bill bill=createBillService.generateBill(createBillRequest);
        return new ResponseEntity<>(new GenericResponse<>("Bill generated successfully",bill), HttpStatus.CREATED);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getAllBills(@PathVariable("customerId")String customerId){
        List<Bill> bills=getBillService.getAllBillsForCustomer(customerId);
        return new ResponseEntity<>(new GenericResponse<>("Success",bills),HttpStatus.OK);
    }

    @GetMapping("/pending/all")
    public ResponseEntity<?> getAllPendingBills() {
        List<Bill> bills= getBillService.getAllPendingBills();
        return new ResponseEntity<>(new GenericResponse<>("Success",bills),HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}/pending")
    public ResponseEntity<?> getPendingBillsForCustomer(@PathVariable String customerId) {
        List<Bill> bills= getBillService.getPendingBillsForCustomer(customerId);
        return new ResponseEntity<>(new GenericResponse<>("Success",bills),HttpStatus.OK);
    }

    @GetMapping("/{billId}")
    public ResponseEntity<?> getBillById(@PathVariable("billId")String billId){
        Bill bill=getBillService.getBillById(billId);
        return new ResponseEntity<>(new GenericResponse<>("Success",bill),HttpStatus.OK);
    }

    @PutMapping("/{billId}/paid")
    public ResponseEntity<?> markBillAsPaid(@PathVariable("billId")String billId,
                                @RequestBody @Valid MarkBillAsPaidRequest markBillAsPaidRequest)
    {
        Bill bill=billPaymentService.markBillAsPaid(billId,markBillAsPaidRequest);
        return new ResponseEntity<>(new GenericResponse<>("Success",bill),HttpStatus.OK);
    }

    @DeleteMapping("/{billId}")
    public ResponseEntity<?> deleteBill(@PathVariable("billId")String billId){
        deleteBillService.deleteBill(billId);
        return new ResponseEntity<>(new GenericResponse<>("Bill with given Id deleted",null),HttpStatus.OK);
    }

    @PostMapping("/{billId}/invoice/details")
    public ResponseEntity<?> getInvoiceDetails(@PathVariable("billId") String billId, @RequestBody @Valid GetInvoiceDetailsRequest
                                               getInvoiceDetailsRequest) throws IOException, DocumentException {
        GetInvoiceResponse getInvoiceResponse=getInvoiceService.getInvoiceDetails(billId,getInvoiceDetailsRequest);
        return new ResponseEntity<>(new GenericResponse<>("Success",getInvoiceResponse),HttpStatus.OK);
    }

    @GetMapping("/{billId}/invoice/pdf")
    public ResponseEntity<byte[]> generateInvoice(@PathVariable String billId) throws IOException, DocumentException {
        // Generate PDF as byte array
        ByteArrayInputStream pdfStream = invoiceGenerationService.generateInvoice(billId);

        // Set headers for PDF download
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=invoice.pdf");

        // Return PDF as response
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdfStream.readAllBytes());
    }

    @PostMapping("/{billId}/send-invoice")
    public ResponseEntity<?> sendInvoiceEmail(@PathVariable String billId) throws MessagingException, DocumentException, IOException {
        String message= sendInvoiceMailService.sendInvoiceEmailService(billId);
        return new ResponseEntity<>(new GenericResponse<>("Mail Sent Successfully",message),HttpStatus.OK);
    }
}
