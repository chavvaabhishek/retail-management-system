package com.rm.dto;

import com.rm.entity.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillResponse {

    private Long id;

    private String invoiceNumber;

    private String customerEmail;

    private BigDecimal subtotal;

    private BigDecimal discount;

    private BigDecimal totalAmount;

    private Integer earnedPoints;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private List<BillItemResponse> items;


}
