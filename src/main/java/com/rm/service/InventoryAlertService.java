package com.rm.service;

import com.rm.entity.Product;
import com.rm.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryAlertService {

    private final ProductRepository productRepository;

    private final EmailService emailService;

    @Scheduled(cron = "0 0 9 * * *")
    public void checkLowStock() {

        List<Product> products =
                productRepository
                        .findByStockQuantityLessThan(10);

        if(products.isEmpty()) {
            return;
        }

        StringBuilder body =
                new StringBuilder();

        body.append("Low Stock Products\n\n");

        for(Product product : products) {

            body.append(product.getName())
                    .append(" - ")
                    .append(product.getStockQuantity())
                    .append("\n");
        }

        emailService.sendEmail(
                "admin@gmail.com",
                "Low Stock Alert",
                body.toString()
        );
    }

}
