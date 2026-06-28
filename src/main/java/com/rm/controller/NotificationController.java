package com.rm.controller;

import com.rm.dto.NotificationResponse;
import com.rm.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public List<NotificationResponse> getNotifications() {

        return notificationService.getMyNotifications();
    }

    @GetMapping("/unread-count")
    public Long unreadCount() {

        return notificationService.getUnreadCount();
    }

    @PutMapping("/{id}/read")
    public String markAsRead(
            @PathVariable Long id
    ) {

        notificationService.markAsRead(id);

        return "Notification marked as read";
    }

    @DeleteMapping("/{id}")
    public String deleteNotification(
            @PathVariable Long id
    ) {

        notificationService.deleteNotification(id);

        return "Notification deleted";
    }
}