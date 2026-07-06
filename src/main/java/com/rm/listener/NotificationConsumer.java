package com.rm.listener;

import com.rm.dto.event.PaymentEvent;
import com.rm.entity.Bill;
import com.rm.entity.NotificationType;
import com.rm.repository.BillRepository;
import com.rm.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final BillRepository billRepository;
    private final NotificationService notificationService;

    @KafkaListener(
            topics = "payment-events",
            groupId = "notification-group"
    )
    public void createNotification(
            PaymentEvent event
    ) {

        Bill bill =
                billRepository.findById(
                        event.getBillId()
                ).orElseThrow();

        notificationService.createNotification(

                bill.getCustomer(),

                "Payment Successful",

                "Payment received for invoice "
                        + bill.getInvoiceNumber(),

                NotificationType.PAYMENT

        );

        System.out.println(
                "Notification Created"
        );

    }

}