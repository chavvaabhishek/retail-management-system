package com.rm.repository;

import com.rm.entity.Bill;
import com.rm.entity.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BillItemRepository
        extends JpaRepository<BillItem, Long> {
    @Query("""
SELECT b.productName,
       SUM(b.quantity)
FROM BillItem b
GROUP BY b.productName
ORDER BY SUM(b.quantity) DESC
""")
    List<Object[]> getTopSellingProducts();
}