package com.rm.controller;

import com.rm.dto.ReturnRequestDto;
import com.rm.service.ReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/returns")
public class ReturnController {

    private final ReturnService returnService;

    @PostMapping
    public String createReturnRequest(
            @RequestBody ReturnRequestDto request,
            Authentication authentication
    ) {

        return returnService.createReturnRequest(
                request,
                authentication
        );
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveReturn(
            @PathVariable Long id
    ) {

        return returnService.approveReturn(
                id
        );
    }
}
