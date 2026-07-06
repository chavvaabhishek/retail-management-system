package com.rm.dto;

import com.rm.entity.FailedEventStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RetryHistoryResponse {

    private LocalDateTime retryTime;

    private FailedEventStatus status;

    private String message;

    private String processedBy;
}