package com.rm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "failed_events")
public class FailedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventId;

    private String invoiceNumber;

    private String topic;

    @Column(length = 1000)
    private String errorMessage;

    @Lob
    private String stackTrace;

    private Integer retryCount;

    private LocalDateTime failedAt;

    @Enumerated(EnumType.STRING)
    private FailedEventStatus status;

    @OneToMany(
            mappedBy = "failedEvent",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RetryHistory> retryHistory =
            new ArrayList<>();

    @Lob
    private String payload;
}