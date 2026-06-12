package com.rm.repository;

import com.rm.entity.Attendance;
import com.rm.entity.Employee;
import com.rm.entity.LeaveRequest;
import com.rm.entity.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LeaveRequestRepository
        extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployee(Employee employee);

    boolean existsByEmployeeAndStatusAndFromDateLessThanEqualAndToDateGreaterThanEqual(
            Employee employee,
            LeaveStatus status,
            LocalDate date1,
            LocalDate date2
    );

//    Optional<Attendance>
//    findByEmployeeAndAttendanceDate(
//            Employee employee,
//            LocalDate attendanceDate
//    );
}