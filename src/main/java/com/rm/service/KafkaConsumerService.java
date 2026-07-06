package com.rm.service;

import com.rm.dto.event.PaymentEvent;
import com.rm.entity.Bill;
import com.rm.entity.NotificationType;
import com.rm.repository.BillRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final PdfService pdfService;

    private final EmailService emailService;

    private final NotificationService notificationService;

    private final BillRepository billRepository;
//    @KafkaListener(
//            topics = "payment-events",
//            groupId = "retail-group"
//    )
//    public void consume(
//            PaymentEvent event
//    ){
//
//            Bill bill =
//                    billRepository.findById(
//                            event.getBillId()
//                    ).orElseThrow();
//
//            byte[] pdf =
//                    pdfService.generateInvoicePdf(
//                            bill.getId()
//                    );
//
//            emailService.sendEmailWithAttachment(
//                    bill.getCustomer().getEmail(),
//                    "Invoice Generated",
//                    "Thank you for shopping",
//                    pdf,
//                    "invoice.pdf"
//            );
//
//            notificationService.createNotification(
//
//                    bill.getCustomer(),
//
//                    "Payment Successful",
//
//                    "Payment received for invoice "
//                            + bill.getInvoiceNumber(),
//
//                    NotificationType.PAYMENT
//            );
//
//        System.out.println("Thread : " + Thread.currentThread().getName());
//
//        System.out.println(event.getInvoiceNumber());
//
//    }




//    @KafkaListener(
//            topics = "payment-events",
//            groupId = "retail-group"
//    )
//    public void consume(
//
//            PaymentEvent event,
//
//            @Header(KafkaHeaders.RECEIVED_TOPIC)
//            String topic,
//
//            @Header(KafkaHeaders.RECEIVED_PARTITION)
//            int partition,
//
//            @Header(KafkaHeaders.OFFSET)
//            long offset,
//
//            @Header(KafkaHeaders.RECEIVED_KEY)
//            String key
//
//    ) {
//
//        System.out.println("--------------------------------");
//
//        System.out.println("Topic      : " + topic);
//
//        System.out.println("Partition  : " + partition);
//
//        System.out.println("Offset     : " + offset);
//
//        System.out.println("Key        : " + key);
//
//        System.out.println("Invoice    : " + event.getInvoiceNumber());
//
//        System.out.println("--------------------------------");
//    }
    }