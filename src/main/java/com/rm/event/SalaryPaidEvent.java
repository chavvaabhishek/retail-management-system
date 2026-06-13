package com.rm.event;

import com.rm.entity.SalaryPayment;
import lombok.Getter;

@Getter
public class SalaryPaidEvent {

    private final SalaryPayment salaryPayment;

    public SalaryPaidEvent(
            SalaryPayment salaryPayment
    ) {
        this.salaryPayment = salaryPayment;
    }
}