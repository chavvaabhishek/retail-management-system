package com.rm.repository;

import com.rm.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryTransactionRepository
        extends JpaRepository<InventoryTransaction, Long> {

    List<InventoryTransaction> findByProductId(Long productId);

    List<InventoryTransaction> findByReferenceNumber(
            String referenceNumber
    );
}