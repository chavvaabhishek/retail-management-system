package com.rm.controller;

import com.rm.dto.ProductRequest;
import com.rm.dto.RestockRequest;
import com.rm.dto.UpdatePriceRequest;
import com.rm.entity.Product;
import com.rm.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(
        name = "Product APIs",
        description = "Operations related to products"
)
public class ProductController {

    private final ProductService productService;

    // ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(
            summary = "Create Product",
            description = "Creates a new product"
    )
    public Product createProduct(
            @RequestBody ProductRequest request
    ) {

        return productService.createProduct(request);
    }

    // ADMIN + CASHIER
    @PreAuthorize("hasAnyRole('ADMIN','CASHIER')")
    @GetMapping
    public List<Product> getAllProducts() {

        return productService.getAllProducts();
    }

    // ADMIN + CASHIER
    @PreAuthorize("hasAnyRole('ADMIN','CASHIER')")
    @GetMapping("/barcode/{barcode}")
    public Product getByBarcode(
            @PathVariable String barcode
    ) {

        return productService.getByBarcode(barcode);
    }

    @PutMapping("/restock/{id}")
    public Product restockProduct(
            @PathVariable Long id,
            @RequestBody RestockRequest request
    ) {

        return productService.restockProduct(
                id,
                request.getQuantity()
        );
    }

    @PutMapping("/restock/barcode/{barcode}")
    public Product restockByBarcode(
            @PathVariable String barcode,
            @RequestBody RestockRequest request
    ) {

        return productService.restockByBarcode(
                barcode,
                request.getQuantity()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/price")
    public Product updatePrice(
            @PathVariable Long id,
            @RequestBody UpdatePriceRequest request
    ) {

        return productService.updatePrice(
                id,
                request.getPrice()
        );
    }

    @PostMapping(
            "/{productId}/image"
    )
    public Product uploadImage(
            @PathVariable Long productId,
            @RequestParam("file")
            MultipartFile file
    ) {

        return productService
                .uploadImage(
                        productId,
                        file
                );
    }
}