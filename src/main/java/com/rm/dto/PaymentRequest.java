package com.rm.dto;

import com.rm.entity.PaymentMethod;
import lombok.Data;

@Data
public class PaymentRequest {

    private Long billId;

    private PaymentMethod paymentMethod;
}