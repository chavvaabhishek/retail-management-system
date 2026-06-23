package com.rm.listener;

import com.rm.entity.Bill;
import com.rm.entity.PaymentStatus;
import com.rm.event.BillCreatedEvent;
import com.rm.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailEventListener {

    private final EmailService emailService;

    @EventListener
    public void handleBillCreated(
            BillCreatedEvent event
    ) {

        Bill bill =
                event.getBill();

        if(bill.getCustomer() == null) {
            return;
        }

//        if (!bill.getPaymentStatus()
//                .equals(PaymentStatus.PAID)) {
//            return;
//        }

        emailService.sendEmail(
                bill.getCustomer().getEmail(),
                "Invoice Generated",
                "Thank you for shopping.\nInvoice Number: "
                        + bill.getInvoiceNumber()
        );
    }
}
