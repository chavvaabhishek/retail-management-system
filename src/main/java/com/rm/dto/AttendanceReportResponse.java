package com.rm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceReportResponse {

    private String employeeName;

    private Integer month;

    private Integer year;

    private Long presentDays;

    private Long absentDays;

    private Long leaveDays;

    private Long halfDays;
}
