package com.rm.dto;

import lombok.Data;

@Data
public class CreateSupplierRequest {

    private String supplierName;

    private String contactPerson;

    private String email;

    private String phone;

    private String address;
}