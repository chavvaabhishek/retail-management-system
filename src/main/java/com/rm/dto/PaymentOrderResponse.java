package com.rm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentOrderResponse {

    private String orderId;

    private Integer amount;

    private String currency;
}
