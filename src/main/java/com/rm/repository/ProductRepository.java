package com.rm.repository;

import com.rm.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    Optional<Product> findByBarcode(String barcode);

    List<Product>
    findByStockQuantityLessThan(Integer stock);
}