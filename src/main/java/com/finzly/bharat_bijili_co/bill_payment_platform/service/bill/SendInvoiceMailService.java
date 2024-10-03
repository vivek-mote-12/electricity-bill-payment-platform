package com.finzly.bharat_bijili_co.bill_payment_platform.service.bill;

import com.finzly.bharat_bijili_co.bill_payment_platform.exception.BillAlreadyMarkedAsPaidException;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Bill;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.itextpdf.text.DocumentException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class SendInvoiceMailService {
    private final GetBillService getBillService;
    private final InvoiceGenerationService invoiceGenerationService;
    private final JavaMailSender javaMailSender;

    public SendInvoiceMailService(JavaMailSender javaMailSender,GetBillService getBillService,InvoiceGenerationService invoiceGenerationService){
        this.getBillService=getBillService;
        this.invoiceGenerationService=invoiceGenerationService;
        this.javaMailSender=javaMailSender;
    }

    public String sendInvoiceEmailService(String billId) throws DocumentException, IOException, MessagingException {

        // Fetch the bill by id
        Bill bill = getBillService.getBillById(billId);

        // Check if the bill is pending
        if (bill.getIsPaid()) {
            throw new BillAlreadyMarkedAsPaidException("Bill already paid. No need to send invoice mail.");
        }

        // Get the customer details
        Customer customer = bill.getCustomer();

        // Generate invoice PDF
        ByteArrayInputStream pdfStream = invoiceGenerationService.generateInvoice(billId);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(customer.getEmail());
        helper.setSubject("Pending Bill Reminder");
        helper.setText("Please find the attached invoice for your pending bill");

        // Attach PDF
        helper.addAttachment("invoice.pdf", new ByteArrayResource(pdfStream.readAllBytes()));

        javaMailSender.send(message);

        return "Invoice sent to "+customer.getName()+" via email";
    }
}
