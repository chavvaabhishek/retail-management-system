package com.rm.scheduler;

import com.rm.service.BirthdayService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BirthdayScheduler {

    private final BirthdayService birthdayService;
//@Scheduled(fixedRate = 30000)
    @Scheduled(cron = "0 0 9 * * *")
    public void sendBirthdayEmails() {

        birthdayService.sendBirthdayWishes();
    }
}
