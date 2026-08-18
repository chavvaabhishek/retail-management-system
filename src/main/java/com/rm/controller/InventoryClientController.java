package com.rm.controller;

import com.rm.dto.InventoryResponse;
import com.rm.service.InventoryClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory-client")
@RequiredArgsConstructor
public class InventoryClientController {

    private final InventoryClientService inventoryClientService;

    @GetMapping("/{productId}")
    public InventoryResponse getInventory(
            @PathVariable Long productId
    ) {
        return inventoryClientService.getInventory(productId);
    }
}