package com.rm.service;

import com.rm.dto.FailedEventResponse;
import com.rm.dto.RetryHistoryResponse;
import com.rm.entity.FailedEvent;
import com.rm.entity.FailedEventStatus;
import com.rm.repository.FailedEventRepository;
import com.rm.repository.RetryHistoryRepository;
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

    public void saveFailedEvent(

            String invoiceNumber,

            String topic,

            Exception exception,

            Integer retryCount

    ) {

        FailedEvent event =
                FailedEvent.builder()

                        .eventId(
                                UUID.randomUUID().toString()
                        )

                        .invoiceNumber(invoiceNumber)

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

}