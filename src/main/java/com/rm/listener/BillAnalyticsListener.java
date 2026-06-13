package com.rm.listener;

import com.rm.event.BillCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class BillAnalyticsListener {

    @Async
    @EventListener
    public void handleBillCreated(
            BillCreatedEvent event
    ) {

        System.out.println(
                "Analytics Updated For Bill : "
                        + event.getBill()
                        .getInvoiceNumber()
        );
    }
}
