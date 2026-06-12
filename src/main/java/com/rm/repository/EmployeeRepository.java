package com.rm.repository;

import com.rm.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    List<Employee> findByActiveTrue();

    Optional<Employee> findByUserEmail(
            String email
    );
}