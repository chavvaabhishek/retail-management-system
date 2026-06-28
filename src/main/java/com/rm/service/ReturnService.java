package com.rm.service;

import com.rm.dto.ReturnRequestDto;
import com.rm.entity.*;
import com.rm.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReturnService {

    private final ReturnRequestRepository returnRequestRepository;
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final AuditService auditService;

    public String createReturnRequest(
            ReturnRequestDto request,
            Authentication authentication
    ) {

        Bill bill =
                billRepository.findById(
                        request.getBillId()
                ).orElseThrow(
                        () -> new RuntimeException(
                                "Bill not found"
                        )
                );

        User customer =
                userRepository.findByEmail(
                        authentication.getName()
                ).orElseThrow(
                        () -> new RuntimeException(
                                "Customer not found"
                        )
                );

        if(!bill.getCustomer()
                .getId()
                .equals(customer.getId())) {

            throw new RuntimeException(
                    "This bill does not belong to you"
            );
        }

        ReturnRequest returnRequest =
                ReturnRequest.builder()
                        .bill(bill)
                        .customer(customer)
                        .reason(request.getReason())
                        .status(ReturnStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .refundAmount(
                                bill.getTotalAmount()
                        )
                        .build();

        returnRequestRepository.save(
                returnRequest
        );

        return "Return request submitted";
    }

    @Transactional
    public String approveReturn(
            Long returnId
    ) {

        ReturnRequest returnRequest =
                returnRequestRepository.findById(
                        returnId
                ).orElseThrow(
                        () -> new RuntimeException(
                                "Return request not found"
                        )
                );

        if(returnRequest.getStatus()
                != ReturnStatus.PENDING) {

            throw new RuntimeException(
                    "Return already processed"
            );
        }

        Bill bill = returnRequest.getBill();

        for(BillItem item : bill.getItems()) {

            Product product =
                    productRepository
                            .findByBarcode(
                                    item.getBarcode()
                            )
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "Product not found"
                                    )
                            );

            product.setStockQuantity(
                    product.getStockQuantity()
                            + item.getQuantity()
            );

            productRepository.save(product);

            InventoryTransaction transaction =
                    InventoryTransaction.builder()
                            .product(product)
                            .quantity(item.getQuantity())
                            .transactionType(
                                    InventoryTransactionType.RETURN
                            )
                            .referenceNumber(
                                    bill.getInvoiceNumber()
                            )
                            .createdAt(
                                    LocalDateTime.now()
                            )
                            .createdBy("SYSTEM")
                            .build();

            inventoryTransactionRepository
                    .save(transaction);
        }

        returnRequest.setStatus(
                ReturnStatus.REFUNDED
        );

        returnRequestRepository.save(
                returnRequest
        );



        bill.setPaymentStatus(
                PaymentStatus.REFUNDED
        );

        billRepository.save(bill);

        User customer = bill.getCustomer();

        Integer currentPoints =
                customer.getLoyaltyPoints();

        if(currentPoints == null) {
            currentPoints = 0;
        }

        customer.setLoyaltyPoints(
                Math.max(
                        0,
                        currentPoints - bill.getEarnedPoints()
                )
        );

        userRepository.save(customer);

        returnRequest.setStatus(
                ReturnStatus.REFUNDED
        );

        returnRequestRepository.save(
                returnRequest
        );

        auditService.log(
                "ADMIN",
                "RETURN_APPROVED",
                "ReturnRequest",
                returnRequest.getId(),
                "Refund approved"
        );
        return "Refund approved";
    }
}