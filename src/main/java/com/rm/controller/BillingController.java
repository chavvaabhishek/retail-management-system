package com.rm.controller;

import com.rm.dto.BillResponse;
import com.rm.dto.CreateBillRequest;
import com.rm.dto.InvoiceResponse;
import com.rm.entity.Bill;
import com.rm.service.BillingService;
import com.rm.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;
    private final RefundService refundService;



    @PreAuthorize(
            "hasAnyRole('ADMIN','CASHIER')"
    )
    @PostMapping
    public BillResponse createBill(
            @RequestBody CreateBillRequest request,
            Authentication authentication
    ) {

        return billingService.createBill(
                request,
                authentication
        );
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','CASHIER')"
    )
    @GetMapping("/{id}")
    public BillResponse getBill(
            @PathVariable Long id
    ) {

        return billingService.getBill(id);
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','CASHIER')"
    )

    @GetMapping
    public List<Bill> getAllBills() {

        return billingService.getAllBills();
    }

    @GetMapping("/cashier/{email}")
    public List<Bill> getBillsByCashier(
            @PathVariable String email
    ) {

        return billingService.getBillsByCashier(email);
    }

    @GetMapping("/invoice/{invoiceNumber}")
    public Bill getInvoice(
            @PathVariable String invoiceNumber
    ) {

        return billingService
                .getInvoice(invoiceNumber);
    }

    @GetMapping("/invoice/bill/{id}")
    public InvoiceResponse generateInvoice(
            @PathVariable Long id
    ) {

        return billingService.generateInvoice(id);
    }

    @GetMapping("/customers/{id}/points")
    public Integer getPoints(
            @PathVariable Long id
    ) {

        return billingService.getPoints(id);
    }


    @PostMapping("/refund/{billId}")
    public ResponseEntity<String> refundBill(
            @PathVariable Long billId
    ) {

        return ResponseEntity.ok(
                refundService.refundBill(billId)
        );
    }


}