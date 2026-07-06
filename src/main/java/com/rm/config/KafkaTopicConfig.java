package com.rm.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic paymentTopic() {

        return new NewTopic(
                "payment-events",
                3,
                (short) 1
        );
    }

    @Bean
    public NewTopic inventoryTopic() {

        return new NewTopic(
                "inventory-events",
                3,
                (short) 1
        );
    }

    @Bean
    public NewTopic couponTopic() {

        return new NewTopic(
                "coupon-events",
                3,
                (short) 1
        );
    }

    @Bean
    public NewTopic notificationTopic() {

        return new NewTopic(
                "notification-events",
                3,
                (short) 1
        );
    }

    @Bean
    public NewTopic auditTopic() {

        return new NewTopic(
                "audit-events",
                3,
                (short) 1
        );
    }

}