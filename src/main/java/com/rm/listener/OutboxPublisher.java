package com.rm.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rm.dto.event.PaymentEvent;
import com.rm.entity.OutboxEvent;
import com.rm.repository.OutboxEventRepository;
import com.rm.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxEventRepository outboxRepository;
    private final KafkaProducerService kafkaProducerService;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    public void publishEvents() {

        List<OutboxEvent> events =
                outboxRepository.findByPublishedFalse();

        for (OutboxEvent event : events) {

            try {

                PaymentEvent paymentEvent =
                        objectMapper.readValue(
                                event.getPayload(),
                                PaymentEvent.class
                        );

                kafkaProducerService.publishPayment(
                        paymentEvent
                );

                event.setPublished(true);

                outboxRepository.save(event);

            } catch (Exception e) {

                System.out.println(
                        "Failed to publish outbox event: "
                                + event.getEventId()
                );
            }
        }
    }
}