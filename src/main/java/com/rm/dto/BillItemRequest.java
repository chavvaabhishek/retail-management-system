package com.rm.dto;

import lombok.Data;

@Data
public class BillItemRequest {

    private String barcode;

    private Integer quantity;
}