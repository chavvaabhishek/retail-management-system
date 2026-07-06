package com.rm.dto;

import com.rm.entity.FailedEventStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FailedEventResponse {

    private Long id;

    private String eventId;

    private String invoiceNumber;

    private String topic;

    private String errorMessage;

    private Integer retryCount;

    private LocalDateTime failedAt;

    private FailedEventStatus status;
}