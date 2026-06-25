package com.rm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private Integer discountPercentage;

    private LocalDate expiryDate;

    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}