package com.rm.dto;

public record InventoryResponse(
        Long productId,
        int availableQuantity
) {
}