package com.rm.listener;

import com.rm.entity.Bill;
import com.rm.entity.User;
import com.rm.event.BillCreatedEvent;
import com.rm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoyaltyPointsListener {

    private final UserRepository userRepository;

    @Async
    @EventListener
    public void handleBillCreated(
            BillCreatedEvent event
    ) {

        Bill bill =
                event.getBill();

        User customer =
                bill.getCustomer();

        Integer currentPoints =
                customer.getLoyaltyPoints();

        if(currentPoints == null) {
            currentPoints = 0;
        }

        int earnedPoints =
                bill.getTotalAmount()
                        .intValue() / 100;

        customer.setLoyaltyPoints(
                currentPoints + earnedPoints
        );

        userRepository.save(customer);

        System.out.println(
                "Loyalty points added"
        );


//        try {
//
//            Thread.sleep(10000);
//
//        } catch (Exception e) {
//
//        }
//
//        System.out.println(
//                "Loyalty updated"
//        );

    }
}