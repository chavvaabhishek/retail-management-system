package com.rm.event;

import com.rm.entity.Bill;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentSuccessfulEvent {

    private Bill bill;
}