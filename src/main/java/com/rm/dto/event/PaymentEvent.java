package com.rm.dto.event;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEvent {

    private Long billId;

    private String invoiceNumber;

    private BigDecimal amount;

    private String customerEmail;
}