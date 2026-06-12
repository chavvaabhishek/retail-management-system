package com.rm.controller;

import com.rm.dto.PaymentRequest;
import com.rm.dto.PaymentResponse;
import com.rm.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
