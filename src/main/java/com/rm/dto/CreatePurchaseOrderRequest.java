package com.rm.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreatePurchaseOrderRequest {

    private Long supplierId;

    private List<PurchaseOrderItemRequest> items;
}
