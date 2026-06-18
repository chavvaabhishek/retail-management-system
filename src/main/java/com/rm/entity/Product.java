package com.rm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String barcode;

    private String category;

    private String brand;

    private BigDecimal price;

    private Integer stockQuantity;

    private Boolean active;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    private String imageUrl;
}