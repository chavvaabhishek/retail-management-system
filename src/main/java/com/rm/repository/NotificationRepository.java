package com.rm.repository;

import com.rm.entity.Notification;
import com.rm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    Long countByUserAndReadFalse(User user);
    List<Notification> findByUserAndReadFalseOrderByCreatedAtDesc(User user);
}