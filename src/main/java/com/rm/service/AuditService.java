package com.rm.service;

import com.rm.entity.AuditLog;
import com.rm.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void log(
            String username,
            String action,
            String entityName,
            Long entityId,
            String description
    ) {

        AuditLog auditLog =
                AuditLog.builder()
                        .username(username)
                        .action(action)
                        .entityName(entityName)
                        .entityId(entityId)
                        .description(description)
                        .createdAt(LocalDateTime.now())
                        .build();

        auditLogRepository.save(auditLog);
    }
}