package com.rm.controller;

import com.rm.dto.PaymentOrderResponse;
import com.rm.dto.PaymentRequest;
import com.rm.dto.PaymentResponse;
import com.rm.dto.VerifyPaymentRequest;
import com.rm.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponse pay(
            @RequestBody PaymentRequest request
    ) {

        return paymentService.makePayment(request);
    }

    @PostMapping("/create/{billId}")
    public PaymentOrderResponse createOrder(
            @PathVariable Long billId
    ) throws Exception {

        return paymentService
                .createOrder(billId);
    }

    @PostMapping("/verify")
    public String verifyPayment(
            @RequestBody
            VerifyPaymentRequest request
    ) throws Exception {

        paymentService.verifyPayment(
                request
        );

        return "Payment Verified";
    }
}
