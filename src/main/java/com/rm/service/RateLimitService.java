package com.rm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final RedisTemplate<String, Integer> redisTemplate;

    private static final int MAX_REQUESTS = 5;

    private static final long WINDOW = 1;

    public boolean isAllowed(String key) {

        Integer count =
                redisTemplate.opsForValue().get(key);

        if (count == null) {

            redisTemplate.opsForValue()
                    .set(key, 1, WINDOW, TimeUnit.MINUTES);

            return true;
        }

        if (count >= MAX_REQUESTS) {
            return false;
        }

        redisTemplate.opsForValue().increment(key);

        return true;
    }
}