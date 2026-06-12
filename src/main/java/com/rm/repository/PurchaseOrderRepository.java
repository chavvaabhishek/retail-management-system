package com.rm.repository;

import com.rm.entity.PurchaseOrder;
import com.rm.entity.PurchaseOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseOrderRepository
        extends JpaRepository<PurchaseOrder, Long> {

    Optional<PurchaseOrder> findByPoNumber(
            String poNumber
    );

    List<PurchaseOrder> findByStatus(
            PurchaseOrderStatus status
    );

    List<PurchaseOrder> findBySupplierId(
            Long supplierId
    );
}