package com.rm.controller;




import com.rm.entity.OptimisticProductTest;
import com.rm.service.OptimisticProductTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/optimistic-test")
@RequiredArgsConstructor
public class OptimisticProductTestController {

    private final OptimisticProductTestService service;

    @PostMapping("/create")
    public OptimisticProductTest createProduct() {

        return service.createTestProduct(
                "Test Laptop",
                1
        );
    }

    @PostMapping("/{id}/purchase")
    public String purchase(
            @PathVariable Long id
    ) {

        return service.purchase(id);
    }
}