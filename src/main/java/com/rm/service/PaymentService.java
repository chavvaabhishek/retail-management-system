package com.rm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.rm.dto.PaymentOrderResponse;
import com.rm.dto.PaymentRequest;
import com.rm.dto.PaymentResponse;
import com.rm.dto.VerifyPaymentRequest;
import com.rm.dto.event.PaymentEvent;
import com.rm.entity.*;
import com.rm.event.PaymentSuccessfulEvent;
import com.rm.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;


    private final OutboxEventRepository outboxEventRepository;
    private final RazorpayClient razorpayClient;

    private final ApplicationEventPublisher eventPublisher;

    private final KafkaProducerService kafkaProducerService;

    private final ObjectMapper objectMapper;

    public PaymentResponse makePayment(
            PaymentRequest request
    ) {

        Bill bill =
                billRepository.findById(
                        request.getBillId()
                ).orElseThrow(
                        () -> new RuntimeException(
                                "Bill not found"
                        )
                );

        Payment payment = Payment.builder()
                .paymentReference(
                        UUID.randomUUID().toString()
                )
                .paymentMethod(
                        request.getPaymentMethod()
                )
                .paymentStatus(
                        PaymentStatus.PAID
                )
                .amount(
                        bill.getTotalAmount()
                )
                .paidAt(LocalDateTime.now())
                .bill(bill)
                .build();

        Payment savedPayment =
                paymentRepository.save(payment);

        bill.setPaymentStatus(
                PaymentStatus.PAID
        );

        billRepository.save(bill);

        return PaymentResponse.builder()
                .paymentReference(
                        savedPayment.getPaymentReference()
                )
                .paymentMethod(
                        savedPayment.getPaymentMethod()
                )
                .paymentStatus(
                        savedPayment.getPaymentStatus()
                )
                .amount(
                        savedPayment.getAmount()
                )
                .build();
    }

    public PaymentOrderResponse createOrder(
            Long billId
    ) throws Exception {

        Bill bill =
                billRepository.findById(billId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Bill not found"
                                ));

        JSONObject options =
                new JSONObject();

        options.put(
                "amount",
                bill.getTotalAmount()
                        .multiply(
                                java.math.BigDecimal.valueOf(100)
                        )
                        .intValue()
        );

        options.put(
                "currency",
                "INR"
        );

        options.put(
                "receipt",
                "bill_" + bill.getId()
        );

        Order order =
                razorpayClient.orders.create(options);

        String orderId =
                order.get("id").toString();

        bill.setRazorpayOrderId(orderId);

        bill.setPaymentStatus(
                PaymentStatus.PENDING
        );

        billRepository.save(bill);

        return PaymentOrderResponse
                .builder()
                .orderId(
                        order.get("id")
                                .toString()
                )
                .amount(
                        order.get("amount")
                )
                .currency(
                        order.get("currency")
                )
                .build();
    }

    @Transactional
    public void verifyPayment(
            VerifyPaymentRequest request
    ) throws JsonProcessingException {

        Bill bill =
                billRepository.findById(
                        request.getBillId()
                ).orElseThrow(
                        () -> new RuntimeException(
                                "Bill not found"
                        )
                );

        bill.setPaymentStatus(
                PaymentStatus.PAID
        );

        bill.setRazorpayPaymentId(
                request.getRazorpayPaymentId()
        );

        bill.setRazorpaySignature(
                request.getRazorpaySignature()
        );

        if(bill.getCoupon() != null) {

            Coupon coupon = bill.getCoupon();

            coupon.setActive(false);

            couponRepository.save(coupon);
        }
        billRepository.save(bill);

//        eventPublisher.publishEvent(
//                new PaymentSuccessfulEvent(bill)
//        );
        PaymentEvent event =
                PaymentEvent.builder()
                        .eventId(UUID.randomUUID().toString())
                        .billId(bill.getId())
                        .invoiceNumber(
                                bill.getInvoiceNumber()
                        )
                        .amount(
                                bill.getTotalAmount()
                        )
                        .customerEmail(
                                bill.getCustomer().getEmail()
                        )
                        .build();

        String payload =
                objectMapper.writeValueAsString(event);
        OutboxEvent outboxEvent =
                OutboxEvent.builder()
                        .eventId(event.getEventId())
                        .eventType("PAYMENT_SUCCESS")
                        .payload(payload)
                        .published(false)
                        .createdAt(LocalDateTime.now())
                        .build();

        outboxEventRepository.save(outboxEvent);
        kafkaProducerService.publishPayment(event);
    }



}
