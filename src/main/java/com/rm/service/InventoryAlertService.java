package com.rm.service;

import com.rm.entity.NotificationType;
import com.rm.entity.Product;
import com.rm.entity.Role;
import com.rm.entity.User;
import com.rm.repository.ProductRepository;
import com.rm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryAlertService {

    private final ProductRepository productRepository;

    private final EmailService emailService;

    private final NotificationService notificationService;

    private final UserRepository userRepository;

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

        List<User> admins =
                userRepository.findByRole(Role.ADMIN);

        for (User admin : admins) {

            for(Product product : products) {
                notificationService.createNotification(

                        admin,

                        "Low Stock",

                        product.getName()
                                + " has only "
                                + product.getStockQuantity()
                                + " units remaining.",

                        NotificationType.LOW_STOCK
                );
            }
        }

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
