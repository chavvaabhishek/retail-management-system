package com.rm.service;

import com.rm.dto.InventoryResponse;
import com.rm.exception.InventoryServiceUnavailableException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class InventoryClientService {

    private final RestTemplate restTemplate;

//    @Retryable(
//            retryFor = InventoryServiceUnavailableException.class,
//            maxAttempts = 3,
//            backoff = @Backoff(delay = 1000)
//    )
   // @RateLimiter(name = "inventoryService")
    @Bulkhead(
            name = "inventoryService",
            type = Bulkhead.Type.SEMAPHORE
    )
//    @CircuitBreaker(
//            name = "inventoryService",
//            fallbackMethod = "inventoryFallback"
//    )
    public InventoryResponse getInventory(Long productId) {

        try {

            System.out.println("Calling Inventory Service...");

            return restTemplate.getForObject(
                    "http://INVENTORY-SERVICE/inventory/{productId}",
                    InventoryResponse.class,
                    productId
            );

        } catch (RestClientException | IllegalArgumentException e) {

            throw new InventoryServiceUnavailableException(
                    "Inventory service is currently unavailable",
                    e
            );
        }
    }
    public InventoryResponse inventoryFallback(
            Long productId,
            Throwable throwable
    ) {

        throw new InventoryServiceUnavailableException(
                "Inventory service is temporarily unavailable",
                throwable
        );
    }
}