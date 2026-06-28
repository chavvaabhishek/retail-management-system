package com.rm.service;

import com.rm.dto.BillItemRequest;
import com.rm.dto.BillItemResponse;
import com.rm.dto.CreateBillRequest;
import com.rm.dto.InvoiceResponse;
import com.rm.dto.BillResponse;
import com.rm.entity.*;
import com.rm.event.BillCreatedEvent;
import com.rm.exception.BillNotFoundException;
import com.rm.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final ProductRepository productRepository;

    private final BillRepository billRepository;

    private final UserRepository userRepository;

    private final InventoryTransactionRepository
            inventoryTransactionRepository;

    private final ApplicationEventPublisher eventPublisher;

    private final CouponRepository couponRepository;

    public BillResponse createBill(
            CreateBillRequest request,
            Authentication authentication
    ) {

        List<BillItem> billItems = new ArrayList<>();

        BigDecimal subtotal = BigDecimal.ZERO;

        int totalItems = 0;

        String invoiceNumber =
                "INV-" + System.currentTimeMillis();

        for (BillItemRequest itemRequest : request.getItems()) {

            Product product =
                    productRepository.findByBarcode(
                            itemRequest.getBarcode()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Product not found"
                            ));

            // STOCK CHECK
            if(product.getStockQuantity()
                    < itemRequest.getQuantity()) {

                throw new RuntimeException(
                        "Insufficient stock for "
                                + product.getName()
                );
            }

            BigDecimal totalPrice =
                    product.getPrice().multiply(
                            BigDecimal.valueOf(
                                    itemRequest.getQuantity()
                            )
                    );



            BillItem billItem = BillItem.builder()
                    .productName(product.getName())
                    .barcode(product.getBarcode())
                    .quantity(itemRequest.getQuantity())
                    .price(product.getPrice())
                    .totalPrice(totalPrice)
                    .build();

            billItems.add(billItem);

            subtotal = subtotal.add(totalPrice);

            totalItems += itemRequest.getQuantity();

            // REDUCE STOCK
            product.setStockQuantity(
                    product.getStockQuantity()
                            - itemRequest.getQuantity()
            );

            productRepository.save(product);

            InventoryTransaction transaction =
                    InventoryTransaction.builder()
                            .product(product)
                            .quantity(itemRequest.getQuantity())
                            .transactionType(
                                    InventoryTransactionType.SALE
                            )
                            .referenceNumber(invoiceNumber)
                            .createdBy(authentication.getName())
                            .createdAt(LocalDateTime.now())
                            .build();

            inventoryTransactionRepository.save(
                    transaction
            );
        }

        // SIMPLE DISCOUNT LOGIC
        BigDecimal discount = BigDecimal.ZERO;

        if(subtotal.compareTo(
                BigDecimal.valueOf(1000)
        ) > 0) {

            discount = subtotal.multiply(
                    BigDecimal.valueOf(0.10)
            );
        }

        BigDecimal total =
                subtotal.subtract(discount);


        User customer =
                userRepository.findByEmail(
                        request.getCustomerEmail()
                ).orElseThrow(
                        () -> new RuntimeException(
                                "Customer not found"
                        )
                );

        Integer availablePoints =
                customer.getLoyaltyPoints();
        if(availablePoints == null) {
            availablePoints = 0;
        }

        Integer redeemPoints =
                request.getPointsToRedeem();

        if(redeemPoints > availablePoints) {
            throw new RuntimeException(
                    "Not enough loyalty points"
            );
        }

        BigDecimal loyaltyDiscount =
                BigDecimal.valueOf(redeemPoints);

        total =
                total.subtract(loyaltyDiscount);

        Coupon coupon = null;

        if(request.getCouponCode() != null &&
                !request.getCouponCode().isBlank()) {

            coupon = couponRepository
                    .findByCode(request.getCouponCode())
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "Invalid Coupon"
                            )
                    );

            if (!coupon.getUser()
                    .getId()
                    .equals(customer.getId())) {

                throw new RuntimeException(
                        "Coupon does not belong to this customer"
                );
            }

            if (coupon.getExpiryDate()
                    .isBefore(LocalDate.now())) {

                throw new RuntimeException(
                        "Coupon expired"
                );
            }

            if (!coupon.getActive()) {

                throw new RuntimeException(
                        "Coupon already used"
                );
            }


            BigDecimal coupondiscount =
                    total.multiply(
                                    BigDecimal.valueOf(
                                            coupon.getDiscountPercentage()
                                    )
                            )
                            .divide(BigDecimal.valueOf(100));

            total = total.subtract(coupondiscount);
        }

        customer.setLoyaltyPoints(
                availablePoints - redeemPoints
        );

        int earnedPoints =
                total.intValue() / 100;

        Integer currentPoints =
                customer.getLoyaltyPoints();

        if(currentPoints == null) {
            currentPoints = 0;
        }
        customer.setLoyaltyPoints(

                currentPoints
                        + earnedPoints
        );
        userRepository.save(customer);

        Bill bill = Bill.builder()
                .subtotal(subtotal)
                .discount(discount)
                .totalAmount(total)
                .totalItems(totalItems)
                .createdAt(LocalDateTime.now())
                .createdBy(authentication.getName())
                .invoiceNumber(invoiceNumber)
                .customer(customer)
                .earnedPoints(earnedPoints)
                .paymentStatus(PaymentStatus.PENDING)
                .coupon(coupon)
                .build();

        bill.setItems(billItems);

        for(BillItem item : billItems){
            item.setBill(bill);
        }

        Bill savedBill = billRepository.save(bill);

        eventPublisher.publishEvent(
                new BillCreatedEvent(savedBill)
        );

        List<BillItemResponse> itemResponses =
                savedBill.getItems()
                        .stream()
                        .map(item -> BillItemResponse.builder()
                                .productName(item.getProductName())
                               // .barcode(item.getBarcode())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .totalPrice(item.getTotalPrice())
                                .build())
                        .toList();

       BillResponse billResponse = BillResponse.builder()
                .id(bill.getId())
                .invoiceNumber(bill.getInvoiceNumber())
                .customerEmail(
                        bill.getCustomer().getEmail()
                )
               .subtotal(bill.getSubtotal())
               .discount(bill.getDiscount())
                .totalAmount(
                        bill.getTotalAmount()
                )

                .earnedPoints(
                        bill.getEarnedPoints()
                )
               .paymentStatus(bill.getPaymentStatus())
               .items(itemResponses)
                .build();

      //  return billRepository.save(savedBill);
        return billResponse;
    }

    public BillResponse getBill(Long id) {

        Bill savedBill = billRepository.findById(id)
                .orElseThrow(() ->
                        new BillNotFoundException(
                                "Bill not found"
                        ));

        List<BillItemResponse> itemResponses =
                savedBill.getItems()
                        .stream()
                        .map(item -> BillItemResponse.builder()
                                .productName(item.getProductName())
                                // .barcode(item.getBarcode())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .totalPrice(item.getTotalPrice())
                                .build())
                        .toList();

        BillResponse billResponse = BillResponse.builder()
                .id(savedBill.getId())
                .invoiceNumber(savedBill.getInvoiceNumber())
                .customerEmail(
                        savedBill.getCustomer().getEmail()
                )
                .subtotal(savedBill.getSubtotal())
                .discount(savedBill.getDiscount())
                .totalAmount(
                        savedBill.getTotalAmount()
                )

                .earnedPoints(
                        savedBill.getEarnedPoints()
                )
                .paymentStatus(savedBill.getPaymentStatus())
                .items(itemResponses)
                .build();
        return billResponse;

    }

    public List<Bill> getAllBills() {

        return billRepository.findAll();
    }

    public List<Bill> getBillsByCashier(
            String email
    ) {

        return billRepository.findByCreatedBy(email);
    }

    public Bill getInvoice(
            String invoiceNumber
    ) {

        return billRepository
                .findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invoice not found"
                        ));
    }

    public InvoiceResponse generateInvoice(Long billId) {

        BillResponse bill = getBill(billId);

        List<BillItemResponse> itemResponses =
                bill.getItems()
                        .stream()
                        .map(item -> BillItemResponse.builder()
                                .productName(item.getProductName())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .totalPrice(item.getTotalPrice())
                                .build())
                        .toList();

        return InvoiceResponse.builder()
                .invoiceNumber(bill.getInvoiceNumber())
             //   .cashier(bill.getCreatedBy())
                .subtotal(bill.getSubtotal())
                .discount(bill.getDiscount())
                .totalAmount(bill.getTotalAmount())
                .items(itemResponses)
                .build();
    }

    public Integer getPoints(Long id) {

        return userRepository
                .findById(id)
                .orElseThrow()
                .getLoyaltyPoints();
    }


}