package com.rm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rm.entity.Employee;
import com.rm.entity.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "salary_payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class    SalaryPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonIgnore
    private Employee employee;

    private Integer month;

    private Integer year;

    private BigDecimal amount;

    private LocalDateTime paidAt;

    private String transactionId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}