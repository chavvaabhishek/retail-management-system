package com.rm.service;

import com.rm.dto.PaymentRequest;
import com.rm.dto.PaymentResponse;
import com.rm.entity.Bill;
import com.rm.entity.Payment;
import com.rm.entity.PaymentStatus;
import com.rm.entity.Product;
import com.rm.repository.BillRepository;
import com.rm.repository.PaymentRepository;
import com.rm.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;

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


}
