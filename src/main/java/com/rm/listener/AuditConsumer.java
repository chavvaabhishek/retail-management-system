package com.rm.listener;

import com.rm.dto.event.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditConsumer {

    @KafkaListener(
            topics = "payment-events",
            groupId = "audit-group"
    )
    public void audit(
            PaymentEvent event
    ) {

        System.out.println(
                "Audit Logged : "
                        + event.getInvoiceNumber()
        );

    }

}