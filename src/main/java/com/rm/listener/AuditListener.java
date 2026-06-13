package com.rm.listener;

import com.rm.event.BillCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AuditListener {

    @Async
    @EventListener
    public void handleBillCreated(
            BillCreatedEvent event
    ) {

        System.out.println(
                "AUDIT -> Bill Created : "
                        + event.getBill()
                        .getInvoiceNumber()
        );
    }
}