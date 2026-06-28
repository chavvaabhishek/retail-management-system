package com.rm.listener;

import com.rm.entity.AuditLog;
import com.rm.event.PurchaseOrderReceivedEvent;
import com.rm.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PurchaseOrderAuditListener {

    private final AuditLogRepository auditLogRepository;

    @Async
    @EventListener
    public void handlePurchaseOrderReceived(
            PurchaseOrderReceivedEvent event
    ) {

        AuditLog log =
                AuditLog.builder()
                        .action("PURCHASE_ORDER_RECEIVED")
                       .username("ADMIN")
                        .description(
                                event.getPurchaseOrder()
                                        .getPoNumber()
                        )
                        .createdAt(
                                LocalDateTime.now()
                        )
                        .build();

        auditLogRepository.save(log);
    }
}