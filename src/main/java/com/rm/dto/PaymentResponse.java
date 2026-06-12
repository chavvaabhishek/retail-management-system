package com.rm.dto;

import com.rm.entity.PaymentMethod;
import com.rm.entity.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentResponse {

    private String paymentReference;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private BigDecimal amount;
}