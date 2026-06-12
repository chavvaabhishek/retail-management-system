package com.rm.service;

import com.rm.entity.*;
import com.rm.repository.BillRepository;
import com.rm.repository.InventoryTransactionRepository;
import com.rm.repository.ProductRepository;
import com.rm.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final ProductRepository productRepository;

    private final BillRepository billRepository;

    private final UserRepository userRepository;

    private final InventoryTransactionRepository
            inventoryTransactionRepository;
    @Transactional
    public String refundBill(Long billId) {

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() ->
                        new RuntimeException("Bill not found"));

        if (bill.getPaymentStatus() == PaymentStatus.REFUNDED) {
            throw new RuntimeException(
                    "Bill already refunded"
            );
        }

        // Restore stock
        for (BillItem item : bill.getItems()) {

            Product product =
                    productRepository.findByBarcode(
                            item.getBarcode()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Product not found"
                            ));

            product.setStockQuantity(
                    product.getStockQuantity()
                            + item.getQuantity()
            );

            InventoryTransaction transaction =
                    InventoryTransaction.builder()
                            .product(product)
                            .quantity(item.getQuantity())
                            .transactionType(
                                    InventoryTransactionType.REFUND
                            )
                            .referenceNumber(
                                    bill.getInvoiceNumber()
                            )
                           // .createdBy(authentication.getName())
                            .createdAt(LocalDateTime.now())
                            .build();

            inventoryTransactionRepository.save(
                    transaction
            );

            productRepository.save(product);
        }

        // Reverse loyalty points
        User customer = bill.getCustomer();

        if (customer != null) {

            Integer currentPoints =
                    customer.getLoyaltyPoints();

            if (currentPoints == null) {
                currentPoints = 0;
            }

            customer.setLoyaltyPoints(
                    Math.max(
                            0,
                            currentPoints - bill.getEarnedPoints()
                    )
            );

            userRepository.save(customer);
        }

        // Update bill status
        bill.setPaymentStatus(
                PaymentStatus.REFUNDED
        );

        billRepository.save(bill);

        return "Bill refunded successfully";
    }
}
