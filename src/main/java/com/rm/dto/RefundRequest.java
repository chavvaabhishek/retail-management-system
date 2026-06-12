package com.rm.dto;

import lombok.Data;

@Data
public class RefundRequest {
    private Long billId;

    private String reason;
}
