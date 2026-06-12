package com.rm.controller;

import com.rm.dto.CreatePurchaseOrderRequest;
import com.rm.entity.PurchaseOrder;
import com.rm.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public PurchaseOrder createPurchaseOrder(
            @RequestBody CreatePurchaseOrderRequest request,
            Authentication authentication
    ) {

        return purchaseOrderService
                .createPurchaseOrder(
                        request,
                        authentication
                );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/receive")
    public String receivePurchaseOrder(
            @PathVariable Long id
    ) {

        return purchaseOrderService
                .receivePurchaseOrder(id);
    }

    @GetMapping
    public List<PurchaseOrder> getAllPurchaseOrders() {

        return purchaseOrderService
                .getAllPurchaseOrders();
    }

    @GetMapping("/{id}")
    public PurchaseOrder getPurchaseOrder(
            @PathVariable Long id
    ) {

        return purchaseOrderService
                .getPurchaseOrder(id);
    }
}