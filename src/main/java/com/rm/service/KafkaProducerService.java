package com.rm.service;

import com.rm.dto.event.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public void publishPayment(
            PaymentEvent event
    ) {

        kafkaTemplate.send(
                "payment-events",
                event.getBillId().toString(),
                event
        );

        System.out.println(
                "Published Payment Event : "
                        + event.getInvoiceNumber()
        );
    }
}