package com.rm.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BillItemResponse {

    private String productName;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal totalPrice;
}