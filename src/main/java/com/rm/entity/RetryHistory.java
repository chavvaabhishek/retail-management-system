package com.rm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "retry_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetryHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Which failed event was retried
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "failed_event_id")
    private FailedEvent failedEvent;

    /**
     * When retry happened
     */
    private LocalDateTime retryTime;

    /**
     * SUCCESS / FAILED
     */
    @Enumerated(EnumType.STRING)
    private FailedEventStatus status;

    /**
     * Error message or success message
     */
    @Column(length = 1000)
    private String message;

    /**
     * Who retried
     */
    private String processedBy;
}