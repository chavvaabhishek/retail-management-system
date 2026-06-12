package com.rm.controller;

import com.rm.dto.CreateSupplierRequest;
import com.rm.dto.SupplierResponse;
import com.rm.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public SupplierResponse createSupplier(
            @RequestBody CreateSupplierRequest request
    ) {

        return supplierService.createSupplier(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<SupplierResponse> getAllSuppliers() {

        return supplierService.getAllSuppliers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public SupplierResponse getSupplier(
                @PathVariable Long id
    ) {

        return supplierService.getSupplier(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteSupplier(
            @PathVariable Long id
    ) {

        supplierService.deleteSupplier(id);
    }
}