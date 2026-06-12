package com.rm.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveRequestDto {

    private LocalDate fromDate;

    private LocalDate toDate;

    private String reason;
}