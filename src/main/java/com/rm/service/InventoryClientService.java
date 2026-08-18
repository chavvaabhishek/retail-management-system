package com.rm.service;

import com.rm.dto.InventoryResponse;
import com.rm.exception.InventoryServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class InventoryClientService {

    private final RestTemplate restTemplate;

    public InventoryResponse getInventory(Long productId) {

        try {

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
}