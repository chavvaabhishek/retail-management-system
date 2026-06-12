package com.rm.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseOrderItemRequest {

    private Long productId;

    private Integer quantity;

    private BigDecimal costPrice;
}
