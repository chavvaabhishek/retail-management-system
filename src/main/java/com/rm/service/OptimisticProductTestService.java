package com.rm.service;



import com.rm.entity.OptimisticProductTest;
import com.rm.repository.OptimisticProductTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OptimisticProductTestService {

    private final OptimisticProductTestRepository repository;

    @Transactional
    public String purchase(Long id) {

//        OptimisticProductTest product =
//                repository.findById(id)
//                        .orElseThrow(() ->
//                                new RuntimeException(
//                                        "Test product not found"
//                                ));

        OptimisticProductTest product =
                repository.findByIdForUpdate(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Test product not found"
                                ));


        System.out.println(
                "THREAD = " +
                        Thread.currentThread().getName() +
                        " | STOCK = " +
                        product.getStock() +
                        " | VERSION = " +
                        product.getVersion()
        );

        if (product.getStock() <= 0) {
            throw new RuntimeException(
                    "OUT OF STOCK"
            );
        }

        /*
         * ONLY FOR TESTING CONCURRENCY
         *
         * This gives another request enough time
         * to read the same stock/version.
         */
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "Thread interrupted",
                    e
            );
        }

        product.setStock(
                product.getStock() - 1
        );

        repository.save(product);

        System.out.println(
                "PURCHASE SUCCESS | THREAD = " +
                        Thread.currentThread().getName()
        );

        return "Purchase successful";
    }

    public OptimisticProductTest createTestProduct(
            String name,
            Integer stock
    ) {

        OptimisticProductTest product =
                new OptimisticProductTest(
                        name,
                        stock
                );

        return repository.save(product);
    }
}