package com.rm.repository;

import com.rm.entity.Attendance;
import com.rm.entity.AttendanceStatus;
import com.rm.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository
        extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByEmployeeAndAttendanceDate(
            Employee employee,
            LocalDate date
    );

    List<Attendance> findByEmployeeId(Long employeeId);

    List<Attendance> findByEmployeeOrderByAttendanceDateDesc(
            Employee employee
    );
    List<Attendance> findByEmployeeAndAttendanceDateBetween(
            Employee employee,
            LocalDate startDate,
            LocalDate endDate
    );

    Long countByEmployeeAndAttendanceDateBetweenAndStatus(
            Employee employee,
            LocalDate startDate,
            LocalDate endDate,
            AttendanceStatus status
    );


}
