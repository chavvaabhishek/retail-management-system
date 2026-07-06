package com.rm.repository;

import com.rm.entity.RetryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RetryHistoryRepository
        extends JpaRepository<RetryHistory,Long> {

    List<RetryHistory> findByFailedEventId(Long failedEventId);

}