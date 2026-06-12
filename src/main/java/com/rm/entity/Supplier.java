package com.rm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String supplierName;

    private String contactPerson;

    @Column(unique = true)
    private String email;

    private String phone;

    private String address;

    private Boolean active;

    private LocalDateTime createdAt;
}