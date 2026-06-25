package com.rm.listener;

import com.rm.entity.Bill;
import com.rm.event.BillCreatedEvent;
import com.rm.service.EmailService;
import com.rm.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceEmailListener {
    private final PdfService pdfService;

    private final EmailService emailService;

    @EventListener
    public void handleBillCreated(
            BillCreatedEvent event
    ) {

        Bill bill =
                event.getBill();

        if(bill.getCustomer() == null) {
            return;
        }

        byte[] pdf =
                pdfService.generateInvoicePdf(
                        bill.getId()
                );

//        emailService.sendEmailWithAttachment(
//                bill.getCustomer().getEmail(),
//                "Invoice Generated",
//                "Thank you for shopping",
//                pdf,
//                "invoice.pdf"
//        );
    }
}