package com.rm.repository;

import com.rm.entity.FailedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailedEventRepository
        extends JpaRepository<FailedEvent,Long> {
}