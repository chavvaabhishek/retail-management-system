package com.rm.listener;

import com.rm.event.SalaryPaidEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SalaryNotificationListener {

    @Async
    @EventListener
    public void handleSalaryPaid(
            SalaryPaidEvent event
    ) {

        System.out.println(
                "Salary credited to : "
                        + event.getSalaryPayment()
                        .getEmployee()
                        .getUser()
                        .getEmail()
        );
    }
}
