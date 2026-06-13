package com.rm.listener;

import com.rm.entity.AuditLog;
import com.rm.entity.Bill;
import com.rm.event.BillCreatedEvent;
import com.rm.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BillAuditListener {

    private final AuditLogRepository auditLogRepository;

    @Async
    @EventListener
    public void handleBillCreated(
            BillCreatedEvent event
    ) {

        Bill bill =
                event.getBill();

        AuditLog log =
                AuditLog.builder()
                        .action("BILL_CREATED")
                        .performedBy(
                                bill.getCreatedBy()
                        )
                        .details(
                                "Invoice : "
                                        + bill.getInvoiceNumber()
                        )
                        .createdAt(
                                LocalDateTime.now()
                        )
                        .build();

        auditLogRepository.save(log);
    }
}
