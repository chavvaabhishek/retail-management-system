package com.rm.controller;

import com.rm.dto.PaymentOrderResponse;
import com.rm.dto.PaymentRequest;
import com.rm.dto.PaymentResponse;
import com.rm.dto.VerifyPaymentRequest;
import com.rm.dto.event.PaymentEvent;
import com.rm.service.FailedEventService;
import com.rm.service.KafkaProducerService;
import com.rm.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final KafkaProducerService kafkaProducerService;
    private final FailedEventService failedEventService;

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

    @PostMapping("/kafka-test")
    public String testKafka() {

        kafkaProducerService.publishPayment(

                PaymentEvent.builder()
                        .billId(1L)
                        .invoiceNumber("INV-1001")
                        .amount(BigDecimal.valueOf(1500))
                        .customerEmail("abc@gmail.com")
                        .build()
        );

        return "Message Sent";
    }

    @PostMapping("/{id}/retry")
    public ResponseEntity<String> retry(

            @PathVariable Long id

    ) throws Exception {

        failedEventService.retryEvent(id);

        return ResponseEntity.ok(

                "Retry initiated."

        );

    }
}
