package com.rm.service;

import com.rm.dto.SalesSummaryResponse;
import com.rm.dto.TopProductResponse;
import com.rm.entity.Product;
import com.rm.repository.BillItemRepository;
import com.rm.repository.BillRepository;
import com.rm.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final BillRepository billRepository;
    private final BillItemRepository billItemRepository;
    private final ProductRepository productRepository;
    LocalDate today = LocalDate.now();

    LocalDateTime start =
            today.atStartOfDay();

    LocalDateTime end =
            today.atTime(23,59,59);

    public SalesSummaryResponse todaySales() {

        return new SalesSummaryResponse(
                billRepository.getTodayRevenue(start,end),
                billRepository.getTodayBillCount(start,end),
                billRepository.getTodayItemsSold(start,end)
        );
    }

    public List<TopProductResponse> topProducts() {

        return billItemRepository
                .getTopSellingProducts()
                .stream()
                .map(row ->
                        new TopProductResponse(
                                (String) row[0],
                                ((Number) row[1]).longValue()
                        )
                )
                .toList();
    }

    public List<Product> lowStockProducts() {

        return productRepository
                .findByStockQuantityLessThan(10);
    }



}
