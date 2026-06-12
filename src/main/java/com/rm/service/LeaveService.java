package com.rm.service;

import com.rm.dto.LeaveRequestDto;
import com.rm.entity.*;
import com.rm.repository.AttendanceRepository;
import com.rm.repository.EmployeeRepository;
import com.rm.repository.LeaveRequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;

    public LeaveRequest applyLeave(
            LeaveRequestDto request,
            String email
    ) {

        Employee employee =
                employeeRepository
                        .findByUserEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found"
                                ));

        if(request.getFromDate()
                .isBefore(LocalDate.now())) {

            throw new RuntimeException(
                    "Cannot apply leave for past dates"
            );
        }

        if(request.getToDate()
                .isBefore(request.getFromDate())) {

            throw new RuntimeException(
                    "Invalid date range"
            );
        }

        LeaveRequest leaveRequest =
                LeaveRequest.builder()
                        .employee(employee)
                        .fromDate(request.getFromDate())
                        .toDate(request.getToDate())
                        .reason(request.getReason())
                        .status(LeaveStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .build();

        return leaveRequestRepository.save(
                leaveRequest
        );
    }

    @Transactional
    public LeaveRequest approveLeave(
            Long leaveId
    ) {

        LeaveRequest leaveRequest =
                leaveRequestRepository
                        .findById(leaveId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Leave request not found"
                                ));

        if(leaveRequest.getStatus()
                != LeaveStatus.PENDING) {

            throw new RuntimeException(
                    "Leave request already processed"
            );
        }

        leaveRequest.setStatus(
                LeaveStatus.APPROVED
        );

        LocalDate currentDate =
                leaveRequest.getFromDate();

        while(!currentDate.isAfter(
                leaveRequest.getToDate()
        )) {

            Attendance existingAttendance =
                    attendanceRepository
                            .findByEmployeeAndAttendanceDate(
                                    leaveRequest.getEmployee(),
                                    currentDate
                            )
                            .orElse(null);

            if(existingAttendance == null) {

                Attendance attendance =
                        Attendance.builder()
                                .employee(
                                        leaveRequest.getEmployee()
                                )
                                .attendanceDate(
                                        currentDate
                                )
                                .status(
                                        AttendanceStatus.LEAVE
                                )
                                .build();

                attendanceRepository.save(
                        attendance
                );
            }

            currentDate =
                    currentDate.plusDays(1);
        }

        return leaveRequestRepository.save(
                leaveRequest
        );
    }

    public LeaveRequest rejectLeave(
            Long leaveId
    ) {

        LeaveRequest leaveRequest =
                leaveRequestRepository
                        .findById(leaveId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Leave request not found"
                                ));

        if(leaveRequest.getStatus()
                != LeaveStatus.PENDING) {

            throw new RuntimeException(
                    "Leave request already processed"
            );
        }

        leaveRequest.setStatus(
                LeaveStatus.REJECTED
        );

        return leaveRequestRepository.save(
                leaveRequest
        );
    }

    public List<LeaveRequest> getMyLeaves(
            String email
    ) {

        Employee employee =
                employeeRepository
                        .findByUserEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found"
                                ));

        return leaveRequestRepository
                .findByEmployee(employee);
    }

    public List<LeaveRequest> getAllLeaves() {

        return leaveRequestRepository.findAll();
    }

    public LeaveRequest getLeaveById(
            Long id
    ) {

        return leaveRequestRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Leave request not found"
                        ));
    }
}