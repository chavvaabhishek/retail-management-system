package com.rm.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

@Entity
@Table(name = "bill_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private String barcode;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    @JsonIgnore//important
    private Bill bill;
}