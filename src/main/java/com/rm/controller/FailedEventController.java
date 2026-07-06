package com.rm.controller;

import com.rm.dto.FailedEventResponse;
import com.rm.dto.RetryHistoryResponse;
import com.rm.service.FailedEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/failed-events")
@RequiredArgsConstructor
public class FailedEventController {

    private final FailedEventService failedEventService;

    @GetMapping
    public List<FailedEventResponse> getAll() {

        return failedEventService.getAllFailedEvents();

    }

    @GetMapping("/{id}/history")
    public List<RetryHistoryResponse> history(

            @PathVariable Long id

    ) {

        return failedEventService.getRetryHistory(id);

    }

}
