package com.rm.service;

import com.rm.dto.CreatePurchaseOrderRequest;
import com.rm.dto.PurchaseOrderItemRequest;
import com.rm.entity.*;
import com.rm.repository.ProductRepository;
import com.rm.repository.PurchaseOrderRepository;
import com.rm.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;

    @Transactional
    public PurchaseOrder createPurchaseOrder(
            CreatePurchaseOrderRequest request,
            Authentication authentication
    ) {

        Supplier supplier =
                supplierRepository.findById(
                        request.getSupplierId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Supplier not found"
                        ));

        PurchaseOrder order =
                PurchaseOrder.builder()
                        .poNumber(
                                "PO-" +
                                        System.currentTimeMillis()
                        )
                        .supplier(supplier)
                        .status(
                                PurchaseOrderStatus.CREATED
                        )
                        .createdAt(
                                LocalDateTime.now()
                        )
                        .createdBy(
                                authentication.getName()
                        )
                        .build();

        List<PurchaseOrderItem> items =
                new ArrayList<>();

        BigDecimal totalAmount =
                BigDecimal.ZERO;

        for (PurchaseOrderItemRequest itemReq :
                request.getItems()) {

            Product product =
                    productRepository.findById(
                            itemReq.getProductId()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Product not found"
                            ));

            BigDecimal lineTotal =
                    itemReq.getCostPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            itemReq.getQuantity()
                                    )
                            );

            PurchaseOrderItem item =
                    PurchaseOrderItem.builder()
                            .purchaseOrder(order)
                            .product(product)
                            .quantity(
                                    itemReq.getQuantity()
                            )
                            .costPrice(
                                    itemReq.getCostPrice()
                            )
                            .totalPrice(lineTotal)
                            .build();

            items.add(item);

            totalAmount =
                    totalAmount.add(lineTotal);
        }

        order.setItems(items);

        order.setTotalAmount(totalAmount);

        return purchaseOrderRepository.save(order);
    }

    @Transactional
    public String receivePurchaseOrder(
            Long purchaseOrderId
    ) {

        PurchaseOrder order =
                purchaseOrderRepository
                        .findById(purchaseOrderId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Purchase Order not found"
                                ));

        if(order.getStatus()
                == PurchaseOrderStatus.RECEIVED) {

            throw new RuntimeException(
                    "Purchase Order already received"
            );
        }

        for(PurchaseOrderItem item :
                order.getItems()) {

            Product product =
                    item.getProduct();

            product.setStockQuantity(
                    product.getStockQuantity()
                            + item.getQuantity()
            );

            productRepository.save(product);
        }

        order.setStatus(
                PurchaseOrderStatus.RECEIVED
        );

        purchaseOrderRepository.save(order);

        return "Inventory Updated Successfully";
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {

        return purchaseOrderRepository.findAll();
    }

    public PurchaseOrder getPurchaseOrder(
            Long id
    ) {

        return purchaseOrderRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Purchase Order not found"
                        ));
    }
}