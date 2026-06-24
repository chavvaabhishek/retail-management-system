package com.rm.listener;

import com.rm.entity.Bill;
import com.rm.event.PaymentSuccessfulEvent;
import com.rm.service.EmailService;
import com.rm.service.PdfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentListener {

    private final PdfService pdfService;

    private final EmailService emailService;

    @Async
    @EventListener
    public void handlePaymentSuccess(
            PaymentSuccessfulEvent event
    ) {

        Bill bill =
                event.getBill();

        log.info(
                "Payment successful for bill {}",
                bill.getId()
        );

        byte[] pdf =
                pdfService.generateInvoicePdf(
                        bill.getId()
                );

        emailService.sendEmailWithAttachment(
                "yourmail@gmail.com",
                "Invoice Generated",
                "Thank you for shopping",
                pdf,
                "invoice-" +
                        bill.getId() +
                        ".pdf"
        );
    }
}