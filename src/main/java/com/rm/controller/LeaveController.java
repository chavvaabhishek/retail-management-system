package com.rm.controller;

import com.rm.dto.LeaveRequestDto;
import com.rm.entity.LeaveRequest;
import com.rm.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping("/apply")
    public LeaveRequest applyLeave(
            @RequestBody LeaveRequestDto request,
            Authentication authentication
    ) {

        return leaveService.applyLeave(
                request,
                authentication.getName()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/approve")
    public LeaveRequest approveLeave(
            @PathVariable Long id
    ) {

        return leaveService.approveLeave(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/reject")
    public LeaveRequest rejectLeave(
            @PathVariable Long id
    ) {

        return leaveService.rejectLeave(id);
    }

    @GetMapping("/my")
    public List<LeaveRequest> getMyLeaves(
            Authentication authentication
    ) {

        return leaveService.getMyLeaves(
                authentication.getName()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<LeaveRequest> getAllLeaves() {

        return leaveService.getAllLeaves();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public LeaveRequest getLeaveById(
            @PathVariable Long id
    ) {

        return leaveService.getLeaveById(id);
    }
}