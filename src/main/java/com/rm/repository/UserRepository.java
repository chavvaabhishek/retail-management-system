package com.rm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.rm.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("""
SELECT u
FROM User u
WHERE u.role = 'CUSTOMER'
AND MONTH(u.dateOfBirth) = :month
AND DAY(u.dateOfBirth) = :day
""")
    List<User> findTodaysCustomerBirthdays(
            @Param("month") int month,
            @Param("day") int day
    );

    @Query("""
SELECT u
FROM User u
WHERE MONTH(u.dateOfBirth) = :month
AND DAY(u.dateOfBirth) = :day
""")
    List<User> findTodaysBirthdays(
            @Param("month") int month,
            @Param("day") int day
    );
}