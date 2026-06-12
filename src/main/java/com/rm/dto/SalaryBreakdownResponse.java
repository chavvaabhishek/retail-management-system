package com.rm.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class SalaryBreakdownResponse {

    private String employeeName;

    private Integer month;

    private Integer year;

    private BigDecimal baseSalary;

    private Long leaveDays;

    private Integer allowedLeaves;

    private Long extraLeaves;

    private BigDecimal deduction;

    private BigDecimal payableSalary;
    private String employeeType;
}