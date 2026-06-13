package com.rm.listener;

import com.rm.entity.Product;
import com.rm.entity.PurchaseOrder;
import com.rm.entity.PurchaseOrderItem;
import com.rm.event.PurchaseOrderReceivedEvent;
import com.rm.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryListener {

    private final ProductRepository productRepository;

    @Async
    @EventListener
    public void handlePurchaseOrderReceived(
            PurchaseOrderReceivedEvent event
    ) {

        PurchaseOrder po =
                event.getPurchaseOrder();

        for(PurchaseOrderItem item :
                po.getItems()) {

            Product product =
                    item.getProduct();

            product.setStockQuantity(
                    product.getStockQuantity()
                            + item.getQuantity()
            );

            productRepository.save(
                    product
            );
        }
    }
}