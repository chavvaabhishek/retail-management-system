package com.rm.scheduler;

import com.rm.entity.*;
import com.rm.repository.AttendanceRepository;
import com.rm.repository.EmployeeRepository;
import com.rm.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AttendanceScheduler {

    private final EmployeeRepository employeeRepository;

    private final AttendanceRepository attendanceRepository;

    private final LeaveRequestRepository leaveRequestRepository;

    @Scheduled(cron = "0 59 23 * * *")
    //@Scheduled(fixedRate = 60000)
    public void markAbsentEmployees() {

        log.info("Running absent scheduler");

        LocalDate today =
                LocalDate.now();

        List<Employee> employees =
                employeeRepository.findByActiveTrue();

        for(Employee employee : employees) {

            boolean attendanceExists =
                    attendanceRepository
                            .findByEmployeeAndAttendanceDate(
                                    employee,
                                    today
                            )
                            .isPresent();

            if(attendanceExists) {
                continue;
            }

            boolean onLeave =
                    leaveRequestRepository
                            .existsByEmployeeAndStatusAndFromDateLessThanEqualAndToDateGreaterThanEqual(
                                    employee,
                                    LeaveStatus.APPROVED,
                                    today,
                                    today
                            );

            if(onLeave) {
                continue;
            }

            Attendance attendance =
                    Attendance.builder()
                            .employee(employee)
                            .attendanceDate(today)
                            .status(
                                    AttendanceStatus.ABSENT
                            )
                            .build();

            attendanceRepository.save(attendance);

            log.info(
                    "Marked employee {} absent",
                    employee.getEmployeeCode()
            );
        }
    }
}