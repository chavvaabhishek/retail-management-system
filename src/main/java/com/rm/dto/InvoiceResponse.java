package com.rm.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class InvoiceResponse {
    private String invoiceNumber;

    private String cashier;

    private BigDecimal subtotal;

    private BigDecimal discount;

    private BigDecimal totalAmount;

    private List<BillItemResponse> items;
}
