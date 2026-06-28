package com.rm.service;

import com.rm.dto.ProductRequest;
import com.rm.entity.*;
import com.rm.repository.InventoryTransactionRepository;
import com.rm.repository.ProductRepository;
import com.rm.repository.PurchaseOrderRepository;
import com.rm.repository.SupplierRepository;
import com.rm.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final InventoryTransactionRepository
            inventoryTransactionRepository;

    private final SupplierRepository supplierRepository;

    private final PurchaseOrderRepository purchaseOrderRepository;

    private final FileStorageService fileStorageService;

    private final AuditService auditService;
    // CREATE PRODUCT
    public Product createProduct(ProductRequest request) {

        if(productRepository.findByBarcode(
                request.getBarcode()
        ).isPresent()) {

            throw new RuntimeException(
                    "Barcode already exists"
            );
        }

        Supplier supplier =
                supplierRepository.findById(
                                request.getSupplierId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Supplier not found"
                                ));
        Product product = Product.builder()
                .name(request.getName())
                .barcode(request.getBarcode())
                .category(request.getCategory())
                .brand(request.getBrand())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .active(true)
                .createdAt(LocalDateTime.now())
                .supplier(supplier)
                .build();

        Product savedProduct =
                productRepository.save(product);


        auditService.log(
                SecurityUtil.getCurrentUsername(),
                "CREATE_PRODUCT",
                "Product",
                savedProduct.getId(),
                "Created product " + product.getName()
        );
        return savedProduct;
    }

    // GET ALL PRODUCTS
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    // GET PRODUCT BY BARCODE

    @Cacheable(
            value = "products",
            key = "#barcode"
    )
    public Product getByBarcode(
            String barcode
    ) {

        System.out.println(
                "Fetching from DB..."
        );

        return productRepository
                .findByBarcode(barcode)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Product not found"
                        )
                );
    }
    @Transactional
    public Product restockProduct(
            Long productId,
            Integer quantity
    ) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                ));

        product.setStockQuantity(
                product.getStockQuantity() + quantity
        );

        InventoryTransaction transaction =
                InventoryTransaction.builder()
                        .product(product)
                        .quantity(quantity)
                        .transactionType(
                                InventoryTransactionType.RESTOCK
                        )
                        .referenceNumber(
                                "RESTOCK-" + System.currentTimeMillis()
                        )
                        // .createdBy(authentication.getName())
                        .createdAt(LocalDateTime.now())
                        .build();

        inventoryTransactionRepository.save(
                transaction
        );

        return productRepository.save(product);
    }

    @Transactional
    public Product restockByBarcode(
            String barcode,
            Integer quantity
    ) {

        Product product =
                productRepository.findByBarcode(barcode)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                ));

        product.setStockQuantity(
                product.getStockQuantity() + quantity
        );

        InventoryTransaction transaction =
                InventoryTransaction.builder()
                        .product(product)
                        .quantity(quantity)
                        .transactionType(
                                InventoryTransactionType.RESTOCK
                        )
                        .referenceNumber(
                                "RESTOCK-" + System.currentTimeMillis()
                        )
                       // .createdBy(authentication.getName())
                        .createdAt(LocalDateTime.now())
                        .build();

        inventoryTransactionRepository.save(
                transaction
        );

        Product restockedproduct=productRepository.save(product);

        auditService.log(
                SecurityUtil.getCurrentUsername(),
                "restockByBarcode",
                "Product",
                product.getId(),
                "Updated product " + product.getName()
        );
        return restockedproduct;
    }

    public List<InventoryTransaction>
    getProductHistory(Long productId) {

        return inventoryTransactionRepository
                .findByProductId(productId);
    }

    @CachePut(
            value = "products",
            key = "#result.barcode"
    )
    @Transactional
    public Product updatePrice(
            Long productId,
            BigDecimal newPrice
    ) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                ));

        if(newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException(
                    "Price must be greater than zero"
            );
        }

        product.setPrice(newPrice);

        return productRepository.save(product);
    }


    @Transactional
    public Product uploadImage(
            Long productId,
            MultipartFile file
    ) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                ));

        String fileName =
                fileStorageService
                        .uploadProductImage(file);

        product.setImageUrl(fileName);

        return productRepository.save(product);
    }

}