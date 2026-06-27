package com.rm.repository;

import com.rm.entity.ReturnRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnRequestRepository
        extends JpaRepository<ReturnRequest, Long> {
}
