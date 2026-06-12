package com.rm.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    private String name;

    private String barcode;

    private String category;

    private String brand;

    private BigDecimal price;

    private Integer stockQuantity;

    private Long supplierId;
}