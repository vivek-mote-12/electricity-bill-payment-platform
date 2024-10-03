package com.finzly.bharat_bijili_co.bill_payment_platform.service.payment;

import com.finzly.bharat_bijili_co.bill_payment_platform.enums.PaymentStatus;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.BillNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.InvoiceCannotBeGeneratedException;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.PaymentNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.ReceiptCannotBeGenerated;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Bill;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Payment;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.payment.PaymentRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class GenerateReceiptService {
    private final PaymentRepository paymentRepository;

    public GenerateReceiptService(PaymentRepository paymentRepository){
        this.paymentRepository=paymentRepository;
    }

    public ByteArrayInputStream generateReceipt(String paymentId) throws IOException, DocumentException {
        // Fetch payment by Id
        Payment payment=paymentRepository.findByPaymentId(paymentId).orElseThrow(()
                -> new PaymentNotFoundException("Payment not found"));

        if(payment.getPaymentStatus()== PaymentStatus.FAILED){
            throw new ReceiptCannotBeGenerated("Receipt cannot be generated for Failed Payments");
        }

        //Fetch Bill details
        Bill bill=payment.getBill();

        // Fetch customer details
        Customer customer = bill.getCustomer();

        // Create PDF
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);

        document.open();

        // Set document margins
        document.setMargins(36, 36, 36, 36); // left, right, top, bottom

        // Add company title
        Paragraph companytitle = new Paragraph("Bharat Bijili Co.", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.CYAN));
        companytitle.setAlignment(Element.ALIGN_CENTER);
        document.add(companytitle);

        // Add centered title with larger font
        Paragraph title = new Paragraph("Receipt", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 26, BaseColor.BLACK));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        // Customer Details
        Paragraph customerTitle = new Paragraph("Customer Details", FontFactory.getFont(FontFactory.HELVETICA, 18, BaseColor.BLACK));
        customerTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(customerTitle);
        document.add(new Paragraph(" "));

        PdfPTable customerTable = new PdfPTable(2);
        customerTable.setWidthPercentage(100);

        // Adjust default cell properties
        PdfPCell cell;
        customerTable.getDefaultCell().setPadding(10); // Add padding

        // Add customer details
        cell = new PdfPCell(new Phrase("Name", FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        customerTable.addCell(cell);

        cell = new PdfPCell(new Phrase(customer.getName(), FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        customerTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Email", FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        customerTable.addCell(cell);

        cell = new PdfPCell(new Phrase(customer.getEmail(), FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        customerTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Phone", FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        customerTable.addCell(cell);

        cell = new PdfPCell(new Phrase(customer.getPhone(), FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        customerTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Address", FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        customerTable.addCell(cell);

        cell = new PdfPCell(new Phrase(customer.getAddress() + ", " + customer.getCity(), FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        customerTable.addCell(cell);

        document.add(customerTable);
        document.add(new Paragraph(" "));

        // Bill Details
        Paragraph billTitle = new Paragraph("Payment Details", FontFactory.getFont(FontFactory.HELVETICA, 18, BaseColor.BLACK));
        billTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(billTitle);
        document.add(new Paragraph(" "));

        PdfPTable billTable = new PdfPTable(2);
        billTable.setWidthPercentage(100);

        // Adjust default cell properties
        billTable.getDefaultCell().setPadding(10); // Add padding

        // Add bill details
        cell = new PdfPCell(new Phrase("Units Consumed", FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        billTable.addCell(cell);

        cell = new PdfPCell(new Phrase(String.valueOf(bill.getUnitsConsumed()), FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        billTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Billing start from", FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        billTable.addCell(cell);

        cell = new PdfPCell(new Phrase(String.valueOf(bill.getStartDate()), FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        billTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Billing upto ", FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        billTable.addCell(cell);

        cell = new PdfPCell(new Phrase(String.valueOf(bill.getEndDate()), FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        billTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Amount Due", FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        billTable.addCell(cell);

        cell = new PdfPCell(new Phrase("$" + bill.getAmountDue(), FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        billTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Discount", FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        billTable.addCell(cell);

        cell = new PdfPCell(new Phrase("$"+Double.toString(bill.getDiscountAmount()), FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        billTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Total Due After Discount", FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        billTable.addCell(cell);

        cell = new PdfPCell(new Phrase("$"+Double.toString(payment.getAmount()), FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        billTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Date", FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        billTable.addCell(cell);

        cell = new PdfPCell(new Phrase(String.valueOf(payment.getPaymentDate()), FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        billTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Method", FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        billTable.addCell(cell);

        cell = new PdfPCell(new Phrase(String.valueOf(payment.getPaymentMethod()), FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        billTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Transaction reference ID", FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        billTable.addCell(cell);

        cell = new PdfPCell(new Phrase(payment.getTxnRefId(), FontFactory.getFont(FontFactory.HELVETICA, 16)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        billTable.addCell(cell);



        document.add(billTable);

        // Add generation date and discount note
        Paragraph footer = new Paragraph("Receipt Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                FontFactory.getFont(FontFactory.HELVETICA, 12));
        footer.setAlignment(Element.ALIGN_RIGHT);
        document.add(footer);

        // Discount note
        Paragraph discountNote = new Paragraph("Verified",
                FontFactory.getFont(FontFactory.HELVETICA, 12));
        discountNote.setAlignment(Element.ALIGN_RIGHT);
        document.add(discountNote);

        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
