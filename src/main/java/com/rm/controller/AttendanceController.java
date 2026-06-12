package com.rm.controller;

import com.rm.dto.AttendanceReportResponse;
import com.rm.entity.Attendance;
import com.rm.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PreAuthorize("hasAnyRole('ADMIN','CASHIER')")
    @PostMapping("/check-in")
    public Attendance checkIn(
            Authentication authentication
    ) {

        return attendanceService.checkIn(
                authentication.getName()
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','CASHIER')")
    @PostMapping("/check-out")
    public Attendance checkOut(
            Authentication authentication
    ) {

        return attendanceService.checkOut(
                authentication.getName()
        );
    }

    @GetMapping("/my-history")
    public List<Attendance> myHistory(
            Authentication authentication
    ) {

        return attendanceService
                .myAttendanceHistory(
                        authentication.getName()
                );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/report/{employeeId}")
    public AttendanceReportResponse report(

            @PathVariable Long employeeId,

            @RequestParam Integer month,

            @RequestParam Integer year
    ) {

        return attendanceService
                .getMonthlyReport(
                        employeeId,
                        month,
                        year
                );
    }
}