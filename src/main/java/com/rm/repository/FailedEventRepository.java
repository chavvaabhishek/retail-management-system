package com.rm.repository;

import com.rm.entity.FailedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FailedEventRepository
        extends JpaRepository<FailedEvent,Long> {

    Optional<FailedEvent> findByInvoiceNumber(String invoiceNumber);
}