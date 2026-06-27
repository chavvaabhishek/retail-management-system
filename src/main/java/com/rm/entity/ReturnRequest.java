package com.rm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "return_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Bill bill;

    @ManyToOne
    private User customer;

    private String reason;

    @Enumerated(EnumType.STRING)
    private ReturnStatus status;

    private LocalDateTime createdAt;

    private BigDecimal refundAmount;
}
