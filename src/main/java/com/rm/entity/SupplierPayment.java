package com.rm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private PurchaseOrder purchaseOrder;

    private BigDecimal amount;

    private String transactionId;

    private LocalDateTime paidAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
