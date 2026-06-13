package com.rm.listener;

import com.rm.entity.AuditLog;
import com.rm.entity.SalaryPayment;
import com.rm.event.SalaryPaidEvent;
import com.rm.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SalaryAuditListener {

    private final AuditLogRepository auditLogRepository;

    @Async
    @EventListener
    public void handleSalaryPaid(
            SalaryPaidEvent event
    ) {

        SalaryPayment payment =
                event.getSalaryPayment();

        AuditLog log =
                AuditLog.builder()
                        .action("SALARY_PAID")
                        .performedBy("ADMIN")
                        .details(
                                payment.getEmployee()
                                        .getUser()
                                        .getEmail()
                        )
                        .createdAt(
                                LocalDateTime.now()
                        )
                        .build();

        auditLogRepository.save(log);
    }
}
