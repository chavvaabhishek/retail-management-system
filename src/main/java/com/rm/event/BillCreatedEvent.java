package com.rm.event;

import com.rm.entity.Bill;
import lombok.Getter;

@Getter
public class BillCreatedEvent {

    private final Bill bill;

    public BillCreatedEvent(
            Bill bill
    ) {
        this.bill = bill;
    }
}