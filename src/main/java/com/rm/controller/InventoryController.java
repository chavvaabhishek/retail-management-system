package com.rm.controller;


import com.rm.entity.InventoryTransaction;
import com.rm.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final ProductService productService;
    @GetMapping("/{productId}/history")
    public List<InventoryTransaction>
    getProductHistory(
            @PathVariable Long productId
    ) {

        return productService
                .getProductHistory(productId);
    }
}
