package com.rm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal subtotal;

    private BigDecimal discount;

    private BigDecimal totalAmount;

    private Integer totalItems;

    private LocalDateTime createdAt;

    // CASHIER WHO CREATED BILL
    private String createdBy;

    @OneToMany(
            mappedBy = "bill",
            cascade = CascadeType.ALL
    )
    private List<BillItem> items;

    @Column(unique = true)
    private String invoiceNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    private Integer earnedPoints;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}