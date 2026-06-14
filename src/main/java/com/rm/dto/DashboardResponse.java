package com.rm.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DashboardResponse {

    private BigDecimal todayRevenue;

    private Long todayBills;

    private Long totalProducts;

    private Long lowStockProducts;
}