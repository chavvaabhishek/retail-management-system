package com.rm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rm.dto.FailedEventResponse;
import com.rm.dto.RetryHistoryResponse;
import com.rm.dto.event.PaymentEvent;
import com.rm.entity.FailedEvent;
import com.rm.entity.FailedEventStatus;
import com.rm.entity.RetryHistory;
import com.rm.repository.FailedEventRepository;
import com.rm.repository.RetryHistoryRepository;
import com.rm.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FailedEventService {

    private final FailedEventRepository repository;
    private final RetryHistoryRepository retryHistoryRepository;

    private final KafkaProducerService kafkaProducerService;

    private final FailedEventRepository failedEventRepository;

    private final ObjectMapper objectMapper;

    public void saveFailedEvent(

            PaymentEvent paymentEvent,

            String topic,

            Exception exception,

            Integer retryCount

    ) throws JsonProcessingException {

        String payload =
                objectMapper.writeValueAsString(paymentEvent);
        FailedEvent event =
                FailedEvent.builder()

                        .eventId(
                                UUID.randomUUID().toString()
                        )

                        .invoiceNumber(paymentEvent.getInvoiceNumber())

                        .topic(topic)

                        .errorMessage(
                                exception.getMessage()
                        )

                        .stackTrace(
                                getStackTrace(exception)
                        )

                        .retryCount(retryCount)

                        .failedAt(LocalDateTime.now())

                        .status(FailedEventStatus.PENDING)

                        .payload(payload)
                        .build();

        repository.save(event);

    }

    private String getStackTrace(Exception exception) {

        StringBuilder builder =
                new StringBuilder();

        for(StackTraceElement element :
                exception.getStackTrace()) {

            builder.append(element.toString())
                    .append("\n");
        }

        return builder.toString();
    }

    public List<FailedEventResponse> getAllFailedEvents() {

        return failedEventRepository.findAll()

                .stream()

                .map(event -> FailedEventResponse.builder()

                        .id(event.getId())

                        .eventId(event.getEventId())

                        .invoiceNumber(event.getInvoiceNumber())

                        .topic(event.getTopic())

                        .errorMessage(event.getErrorMessage())

                        .retryCount(event.getRetryCount())

                        .failedAt(event.getFailedAt())

                        .status(event.getStatus())

                        .build())

                .toList();

    }

    public List<RetryHistoryResponse> getRetryHistory(
            Long failedEventId
    ) {

        return retryHistoryRepository

                .findByFailedEventId(failedEventId)

                .stream()

                .map(history -> RetryHistoryResponse.builder()

                        .retryTime(history.getRetryTime())

                        .status(history.getStatus())

                        .message(history.getMessage())

                        .processedBy(history.getProcessedBy())

                        .build())

                .toList();

    }

    @Transactional
    public void retryEvent(
            Long failedEventId
    ) throws Exception {

        FailedEvent failedEvent =

                failedEventRepository.findById(

                        failedEventId

                ).orElseThrow();

        failedEvent.setStatus(
                FailedEventStatus.RETRYING
        );

        failedEventRepository.save(
                failedEvent
        );

// Save Retry History
        RetryHistory history =
                RetryHistory.builder()
                        .failedEvent(failedEvent)
                        .retryTime(LocalDateTime.now())
                        .status(FailedEventStatus.RETRYING)
                        .message("Manual Retry Started")
                        .processedBy(SecurityUtil.getCurrentUsername())
                        .build();

        retryHistoryRepository.save(history);

   PaymentEvent paymentEvent =

                objectMapper.readValue(

                        failedEvent.getPayload(),

                        PaymentEvent.class

                );

       kafkaProducerService.publishPayment(
                paymentEvent
        );

    }
    public void markResolved(

            String invoiceNumber

    ) {

        FailedEvent failedEvent =

                repository

                        .findByInvoiceNumber(
                                invoiceNumber
                        )

                        .orElse(null);

        if(failedEvent == null) {

            return;

        }

        failedEvent.setStatus(
                FailedEventStatus.RESOLVED
        );

        repository.save(
                failedEvent
        );

    }
}