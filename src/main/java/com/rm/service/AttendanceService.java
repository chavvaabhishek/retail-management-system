package com.rm.service;

import com.rm.dto.AttendanceReportResponse;
import com.rm.entity.Attendance;
import com.rm.entity.AttendanceStatus;
import com.rm.entity.Employee;
import com.rm.repository.AttendanceRepository;
import com.rm.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public Attendance checkIn(
            String email
    ) {

        Employee employee =
                employeeRepository
                        .findByUserEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found"
                                ));

        if(!employee.getActive()) {

            throw new RuntimeException(
                    "Employee is inactive"
            );
        }

        if(attendanceRepository
                .findByEmployeeAndAttendanceDate(
                        employee,
                        LocalDate.now()
                )
                .isPresent()) {

            throw new RuntimeException(
                    "Already checked in today"
            );
        }

        Attendance attendance =
                Attendance.builder()
                        .employee(employee)
                        .attendanceDate(LocalDate.now())
                        .checkInTime(LocalDateTime.now())
                        .status(AttendanceStatus.PRESENT)
                        .build();

        return attendanceRepository.save(attendance);
    }

    @Transactional
    public Attendance checkOut(
            String email
    ) {

        Employee employee =
                employeeRepository
                        .findByUserEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found"
                                ));

        Attendance attendance =
                attendanceRepository
                        .findByEmployeeAndAttendanceDate(
                                employee,
                                LocalDate.now()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Check-in not found"
                                ));

        if(attendance.getCheckOutTime() != null) {

            throw new RuntimeException(
                    "Already checked out"
            );
        }

        attendance.setCheckOutTime(
                LocalDateTime.now()
        );

        return attendanceRepository.save(attendance);
    }

    public List<Attendance> myAttendanceHistory(
            String email
    ) {

        Employee employee =
                employeeRepository
                        .findByUserEmail(email)
                        .orElseThrow();

        return attendanceRepository
                .findByEmployeeOrderByAttendanceDateDesc(
                        employee
                );
    }
    public AttendanceReportResponse getMonthlyReport(
            Long employeeId,
            Integer month,
            Integer year
    ) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow();

        LocalDate start =
                LocalDate.of(year, month, 1);

        LocalDate end =
                start.withDayOfMonth(
                        start.lengthOfMonth()
                );

        List<Attendance> attendanceList =
                attendanceRepository
                        .findByEmployeeAndAttendanceDateBetween(
                                employee,
                                start,
                                end
                        );

        long present =
                attendanceList.stream()
                        .filter(a ->
                                a.getStatus()
                                        == AttendanceStatus.PRESENT)
                        .count();

        long absent =
                attendanceList.stream()
                        .filter(a ->
                                a.getStatus()
                                        == AttendanceStatus.ABSENT)
                        .count();

        long leave =
                attendanceList.stream()
                        .filter(a ->
                                a.getStatus()
                                        == AttendanceStatus.LEAVE)
                        .count();

        long halfDay =
                attendanceList.stream()
                        .filter(a ->
                                a.getStatus()
                                        == AttendanceStatus.HALF_DAY)
                        .count();

        return AttendanceReportResponse.builder()
                .employeeName(
                        employee.getUser().getName()
                )
                .month(month)
                .year(year)
                .presentDays(present)
                .absentDays(absent)
                .leaveDays(leave)
                .halfDays(halfDay)
                .build();
    }
}