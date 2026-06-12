package com.rm.controller;

import com.rm.dto.SalesSummaryResponse;
import com.rm.dto.TopProductResponse;
import com.rm.entity.Product;
import com.rm.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/today")
    public SalesSummaryResponse todaySales() {

        return analyticsService.todaySales();
    }

    @GetMapping("/low-stock")
    public List<Product> lowStockProducts() {

        return analyticsService
                .lowStockProducts();
    }
    //topProducts

    @GetMapping("/top-product")
    public List<TopProductResponse> topProduct() {

        return analyticsService
                .topProducts();
    }

}