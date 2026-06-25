package com.rm.service;

import com.rm.entity.Coupon;
import com.rm.entity.User;
import com.rm.repository.CouponRepository;
import com.rm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BirthdayService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final CouponRepository couponRepository;

    public void sendBirthdayWishes() {

        LocalDate today = LocalDate.now();

        List<User> birthdayUsers =
                userRepository.findTodaysBirthdays(
                        today.getMonthValue(),
                        today.getDayOfMonth()
                );


        for(User user : birthdayUsers) {

            String couponCode =
                    "HBD" + user.getId();

            Coupon coupon =
                    Coupon.builder()
                            .code(couponCode)
                            .discountPercentage(20)
                            .expiryDate(
                                    LocalDate.now().plusDays(7)
                            )
                            .active(true)
                            .user(user)
                            .build();

            couponRepository.save(coupon);

            emailService.sendEmail(
                    user.getEmail(),
                    "Happy Birthday 🎉",
                    buildBirthdayMessage(user)
            );
        }
    }

    private String buildBirthdayMessage(
            User user
    ) {

        String couponCode = "HBD" + user.getId();

        return """
        Dear %s,

        Happy Birthday 🎉

        Use Coupon:

        %s

        for 20%% discount.

        Valid for 7 days.

        Regards,
        Retail Management Team
        """.formatted(
                user.getName(),
                couponCode
        );
    }
}
