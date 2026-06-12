package com.rm.repository;

import com.rm.entity.Employee;
import com.rm.entity.SalaryPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryPaymentRepository
        extends JpaRepository<SalaryPayment, Long> {

    boolean existsByEmployeeAndMonthAndYear(
            Employee employee,
            Integer month,
            Integer year
    );
}