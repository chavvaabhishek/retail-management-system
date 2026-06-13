package com.rm.service;

import com.rm.dto.SalaryBreakdownResponse;
import com.rm.entity.AttendanceStatus;
import com.rm.entity.Employee;
import com.rm.entity.PaymentStatus;
import com.rm.entity.SalaryPayment;
import com.rm.event.SalaryPaidEvent;
import com.rm.repository.AttendanceRepository;
import com.rm.repository.EmployeeRepository;
import com.rm.repository.SalaryPaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaryService {

    private final EmployeeRepository employeeRepository;
    private final SalaryPaymentRepository salaryPaymentRepository;
    private final AttendanceRepository attendanceRepository;
    private final ApplicationEventPublisher eventPublisher;
    @Transactional
    public SalaryPayment paySalary(
            Long employeeId
    ) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found"
                                ));

        int month =
                LocalDate.now().getMonthValue();

        int year =
                LocalDate.now().getYear();

        boolean alreadyPaid =
                salaryPaymentRepository
                        .existsByEmployeeAndMonthAndYear(
                                employee,
                                month,
                                year
                        );

        if(alreadyPaid) {

            throw new RuntimeException(
                    "Salary already paid for this month"
            );
        }

        SalaryPayment payment =
                SalaryPayment.builder()
                        .employee(employee)
                        .month(month)
                        .year(year)
                        .amount(employee.getSalary())
                        .paidAt(LocalDateTime.now())
                        .transactionId(
                                "SAL-"
                                        + System.currentTimeMillis()
                        )
                        .status(PaymentStatus.PAID)
                        .build();

         SalaryPayment salaryPayment=salaryPaymentRepository.save(payment);

        eventPublisher.publishEvent(
                new SalaryPaidEvent(salaryPayment)
        );
        return salaryPayment;
    }

    @Transactional
    public List<SalaryPayment> payAllEmployees() {

        List<Employee> employees =
                employeeRepository.findByActiveTrue();

        List<SalaryPayment> payments =
                new ArrayList<>();

        int month =
                LocalDate.now().getMonthValue();

        int year =
                LocalDate.now().getYear();

        for(Employee employee : employees) {

            boolean alreadyPaid =
                    salaryPaymentRepository
                            .existsByEmployeeAndMonthAndYear(
                                    employee,
                                    month,
                                    year
                            );

            if(alreadyPaid) {
                continue;
            }
            BigDecimal payableSalary =
                    calculateSalary(
                            employee,
                            month,
                            year
                    );

            SalaryPayment payment =
                    SalaryPayment.builder()
                            .employee(employee)
                            .month(month)
                            .year(year)
                            .amount(payableSalary)
                            .paidAt(LocalDateTime.now())
                            .transactionId(
                                    "SAL-"
                                            + System.nanoTime()
                            )
                            .status(PaymentStatus.PAID)
                            .build();

            payments.add(
                    salaryPaymentRepository.save(payment)
            );
        }

        return payments;
    }

    private BigDecimal calculateProbationSalary(
            Employee employee,
            Integer month,
            Integer year
    ) {

        LocalDate start =
                LocalDate.of(year, month, 1);

        LocalDate end =
                start.withDayOfMonth(
                        start.lengthOfMonth()
                );

        Long presentDays =
                attendanceRepository
                        .countByEmployeeAndAttendanceDateBetweenAndStatus(
                                employee,
                                start,
                                end,
                                AttendanceStatus.PRESENT
                        );

        return employee.getDailyWage()
                .multiply(
                        BigDecimal.valueOf(
                                presentDays
                        )
                );
    }

    private BigDecimal calculatePermanentSalary(
            Employee employee,
            Integer month,
            Integer year
    ) {

        LocalDate start =
                LocalDate.of(year, month, 1);

        LocalDate end =
                start.withDayOfMonth(
                        start.lengthOfMonth()
                );

        Long leaveDays =
                attendanceRepository
                        .countByEmployeeAndAttendanceDateBetweenAndStatus(
                                employee,
                                start,
                                end,
                                AttendanceStatus.LEAVE
                        );

        int allowedLeaves =
                employee.getMonthlyLeaveAllowance();

        long extraLeaves =
                Math.max(
                        0,
                        leaveDays - allowedLeaves
                );

        BigDecimal perDaySalary =
                employee.getSalary()
                        .divide(
                                BigDecimal.valueOf(
                                        start.lengthOfMonth()
                                ),
                                2,
                                RoundingMode.HALF_UP
                        );

        BigDecimal deduction =
                perDaySalary.multiply(
                        BigDecimal.valueOf(
                                extraLeaves
                        )
                );

        return employee.getSalary()
                .subtract(deduction);
    }

    private BigDecimal calculateSalary(
            Employee employee,
            Integer month,
            Integer year
    ) {

        long monthsWorked =
                ChronoUnit.MONTHS.between(
                        employee.getJoiningDate(),
                        LocalDate.now()
                );

        if(monthsWorked < 3) {

            return calculateProbationSalary(
                    employee,
                    month,
                    year
            );
        }

        return calculatePermanentSalary(
                employee,
                month,
                year
        );
    }

    public SalaryBreakdownResponse getSalaryBreakdown(
            Long employeeId
    ) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found"
                                ));

        int month =
                LocalDate.now().getMonthValue();

        int year =
                LocalDate.now().getYear();

        LocalDate start =
                LocalDate.of(year, month, 1);

        LocalDate end =
                start.withDayOfMonth(
                        start.lengthOfMonth()
                );

        long monthsWorked =
                ChronoUnit.MONTHS.between(
                        employee.getJoiningDate(),
                        LocalDate.now()
                );

        // PROBATION EMPLOYEE
        if(monthsWorked < 3) {

            Long presentDays =
                    attendanceRepository
                            .countByEmployeeAndAttendanceDateBetweenAndStatus(
                                    employee,
                                    start,
                                    end,
                                    AttendanceStatus.PRESENT
                            );

            BigDecimal payableSalary =
                    employee.getDailyWage()
                            .multiply(
                                    BigDecimal.valueOf(
                                            presentDays
                                    )
                            );

            return SalaryBreakdownResponse.builder()
                    .employeeName(
                            employee.getUser().getName()
                    )
                    .month(month)
                    .year(year)
                    .employeeType("PROBATION")
                    .baseSalary(
                            employee.getDailyWage()
                    )
                    .leaveDays(0L)
                    .allowedLeaves(0)
                    .extraLeaves(0L)
                    .deduction(BigDecimal.ZERO)
                    .payableSalary(payableSalary)
                    .build();
        }

        // PERMANENT EMPLOYEE

        Long leaveDays =
                attendanceRepository
                        .countByEmployeeAndAttendanceDateBetweenAndStatus(
                                employee,
                                start,
                                end,
                                AttendanceStatus.LEAVE
                        );

        Integer allowedLeaves =
                employee.getMonthlyLeaveAllowance();

        long extraLeaves =
                Math.max(
                        0,
                        leaveDays - allowedLeaves
                );

        BigDecimal perDaySalary =
                employee.getSalary()
                        .divide(
                                BigDecimal.valueOf(
                                        start.lengthOfMonth()
                                ),
                                2,
                                RoundingMode.HALF_UP
                        );

        BigDecimal deduction =
                perDaySalary.multiply(
                        BigDecimal.valueOf(
                                extraLeaves
                        )
                );

        BigDecimal payableSalary =
                employee.getSalary()
                        .subtract(deduction);

        return SalaryBreakdownResponse.builder()
                .employeeName(
                        employee.getUser().getName()
                )
                .month(month)
                .year(year)
                .employeeType("PERMANENT")
                .baseSalary(
                        employee.getSalary()
                )
                .leaveDays(leaveDays)
                .allowedLeaves(allowedLeaves)
                .extraLeaves(extraLeaves)
                .deduction(deduction)
                .payableSalary(payableSalary)
                .build();
    }
}