package com.rm.dto;

import lombok.Data;

@Data
public class VerifyPaymentRequest {

    private Long billId;

    private String razorpayOrderId;

    private String razorpayPaymentId;

    private String razorpaySignature;
}