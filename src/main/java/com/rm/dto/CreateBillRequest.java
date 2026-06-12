package com.rm.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateBillRequest {
    private String customerEmail;

    private Integer pointsToRedeem;
    private List<BillItemRequest> items;
}