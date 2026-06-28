package com.rm.service;

import com.rm.dto.NotificationResponse;
import com.rm.entity.*;
import com.rm.repository.NotificationRepository;
import com.rm.repository.UserRepository;
import com.rm.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public void createNotification(
            User user,
            String title,
            String message,
            NotificationType type
    ) {

        Notification notification =
                Notification.builder()
                        .user(user)
                        .title(title)
                        .message(message)
                        .type(type)
                        .read(false)
                        .createdAt(LocalDateTime.now())
                        .build();

        notificationRepository.save(notification);
    }

    public List<NotificationResponse> getMyNotifications() {

        User user =
                userRepository.findByEmail(
                        SecurityUtil.getCurrentUsername()
                ).orElseThrow(() ->
                        new RuntimeException("User not found"));

        return notificationRepository
                .findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(notification ->
                        NotificationResponse.builder()
                                .id(notification.getId())
                                .title(notification.getTitle())
                                .message(notification.getMessage())
                                .type(notification.getType())
                                .read(notification.getRead())
                                .createdAt(notification.getCreatedAt())
                                .build())
                .toList();
    }

    public Long getUnreadCount() {

        User user =
                userRepository.findByEmail(
                        SecurityUtil.getCurrentUsername()
                ).orElseThrow(() ->
                        new RuntimeException("User not found"));

        return notificationRepository
                .countByUserAndReadFalse(user);
    }

    public void markAsRead(Long notificationId) {

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found"
                                ));

        notification.setRead(true);

        notificationRepository.save(notification);
    }

    public void deleteNotification(Long notificationId) {

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found"
                                ));

        notificationRepository.delete(notification);
    }
}