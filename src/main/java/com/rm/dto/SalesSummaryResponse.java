package com.rm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SalesSummaryResponse {
    private BigDecimal totalRevenue;

    private Long totalBills;

    private Integer totalItemsSold;
}
