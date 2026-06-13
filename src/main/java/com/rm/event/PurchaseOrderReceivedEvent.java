package com.rm.event;

import com.rm.entity.PurchaseOrder;
import lombok.Getter;

@Getter
public class PurchaseOrderReceivedEvent {

    private final PurchaseOrder purchaseOrder;

    public PurchaseOrderReceivedEvent(
            PurchaseOrder purchaseOrder
    ) {
        this.purchaseOrder = purchaseOrder;
    }
}