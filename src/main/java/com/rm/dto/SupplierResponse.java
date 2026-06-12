package com.rm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupplierResponse {

    private Long id;

    private String supplierName;

    private String contactPerson;

    private String email;

    private String phone;

    private String address;
}