package com.rm.repository;



import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.rm.entity.OptimisticProductTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OptimisticProductTestRepository
        extends JpaRepository<OptimisticProductTest, Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM OptimisticProductTest p WHERE p.id = :id")
    Optional<OptimisticProductTest> findByIdForUpdate(
            @Param("id") Long id
    );
}