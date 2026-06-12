package com.rm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String poNumber;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private PurchaseOrderStatus status;

    private LocalDateTime createdAt;

    private String createdBy;

    @OneToMany(
            mappedBy = "purchaseOrder",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PurchaseOrderItem> items;
}