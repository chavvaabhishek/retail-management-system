package com.rm.service;

import com.rm.dto.CreateSupplierRequest;
import com.rm.dto.SupplierResponse;
import com.rm.entity.Supplier;
import com.rm.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierResponse createSupplier(
            CreateSupplierRequest request
    ) {

        if(supplierRepository
                .findByEmail(request.getEmail())
                .isPresent()) {

            throw new RuntimeException(
                    "Supplier already exists"
            );
        }

        Supplier supplier = Supplier.builder()
                .supplierName(request.getSupplierName())
                .contactPerson(request.getContactPerson())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        Supplier savedSupplier =
                supplierRepository.save(supplier);

        return mapToResponse(savedSupplier);
    }

    public List<SupplierResponse> getAllSuppliers() {

        return supplierRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public SupplierResponse getSupplier(Long id) {

        Supplier supplier =
                supplierRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Supplier not found"
                                ));

        return mapToResponse(supplier);
    }

    public void deleteSupplier(Long id) {

        supplierRepository.deleteById(id);
    }

    private SupplierResponse mapToResponse(
            Supplier supplier
    ) {

        return SupplierResponse.builder()
                .id(supplier.getId())
                .supplierName(
                        supplier.getSupplierName()
                )
                .contactPerson(
                        supplier.getContactPerson()
                )
                .email(supplier.getEmail())
                .phone(supplier.getPhone())
                .address(supplier.getAddress())
                .build();
    }
}