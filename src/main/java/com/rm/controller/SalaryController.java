package com.rm.controller;

import com.rm.dto.SalaryBreakdownResponse;
import com.rm.entity.SalaryPayment;
import com.rm.service.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salary")
@RequiredArgsConstructor
public class SalaryController {

    private final SalaryService salaryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/pay/{employeeId}")
    public SalaryPayment paySalary(
            @PathVariable Long employeeId
    ) {

        return salaryService
                .paySalary(employeeId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/pay-all")
    public List<SalaryPayment> payAllEmployees() {

        return salaryService
                .payAllEmployees();
    }

    @GetMapping("/breakdown/{employeeId}")
    public SalaryBreakdownResponse getBreakdown(
            @PathVariable Long employeeId
    ) {
        return salaryService
                .getSalaryBreakdown(employeeId);
    }
}
