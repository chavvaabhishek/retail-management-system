package com.rm.listener;

import com.rm.dto.event.PaymentEvent;
import com.rm.entity.Bill;
import com.rm.repository.BillRepository;
import com.rm.service.EmailService;
import com.rm.service.FailedEventService;
import com.rm.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;

@Service
@RequiredArgsConstructor
public class EmailConsumer {

    private final BillRepository billRepository;
    private final PdfService pdfService;
    private final EmailService emailService;
    private final FailedEventService failedEventService;

    @RetryableTopic(

            attempts = "3",

            backoff = @Backoff(
                    delay = 3000
            ),

            dltTopicSuffix = "-dlt"

    )
    @KafkaListener(
            topics = "payment-events",
            groupId = "email-group"
    )
    public void sendInvoice(
            PaymentEvent event
    ) {

        Bill bill = billRepository
                .findById(event.getBillId())
                .orElseThrow();

        byte[] pdf =
                pdfService.generateInvoicePdf(
                        bill.getId()
                );

        emailService.sendEmailWithAttachment(
                                bill.getCustomer().getEmail(),
                                "Invoice Generated",
                                "Thank you for shopping",
                                pdf,
                                "invoice.pdf"
                        );
        System.out.println(
                "Email Sent : "
                        + bill.getInvoiceNumber()
        );
        failedEventService.markResolved(
                event.getInvoiceNumber()
        );
    }

}