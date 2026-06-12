package com.rm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String employeeCode;

    private String designation;

    private BigDecimal salary;

    private LocalDate joiningDate;

    private Boolean active;

    private BigDecimal dailyWage;

    private Integer monthlyLeaveAllowance;

}