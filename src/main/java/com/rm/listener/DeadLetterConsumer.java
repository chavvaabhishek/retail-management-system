package com.rm.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rm.dto.event.PaymentEvent;
import com.rm.service.FailedEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeadLetterConsumer {
    private final FailedEventService failedEventService;

    @KafkaListener(

            topics = "payment-events-dlt",

            groupId = "dlq-group"

    )
    public void consumeDeadLetter(
            PaymentEvent event
    ) throws JsonProcessingException {

        failedEventService.saveFailedEvent(

                event,

                "payment-events",

                new RuntimeException(
                        "Moved to Dead Letter Queue"
                ),

                3

        );
        System.out.println(
                "DLQ Received : "
                        + event.getInvoiceNumber()
        );

    }
}