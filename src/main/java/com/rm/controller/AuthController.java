package com.rm.controller;

import com.rm.dto.*;
import com.rm.entity.Employee;
import com.rm.entity.Role;
import com.rm.repository.EmployeeRepository;
import com.rm.repository.UserRepository;
import com.rm.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rm.entity.User;

import java.time.LocalDate;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final EmployeeRepository employeeRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        return ResponseEntity.ok(
                authService.register(request)
        );
    }

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request
    ) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-user")
    public String createUser(@RequestBody AdminCreateUserRequest request) {

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole()) // ADMIN decides role
                .firstLogin(true)
                .build();

        userRepository.save(user);
        if(request.getRole() != Role.CUSTOMER) {

            if(request.getSalary() == null) {
                throw new RuntimeException(
                        "Salary is required for employees"
                );
            }


            Employee employee =
                    Employee.builder()
                            .user(user)
                            .employeeCode(
                                    "EMP-" + user.getId()
                            )
                            .designation(
                                    request.getRole().name()
                            )
                            .salary(
                                    request.getSalary()
                            )
                            .joiningDate(
                                    LocalDate.now()
                            )
                            .dailyWage(request.getDailyWage())
                            .monthlyLeaveAllowance(request.getMonthlyLeaveAllowance())
                            .active(true)
                            .build();

            employeeRepository.save(employee);
        }



        return "User created by admin";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ) {

        return authService.changePassword(
                request,
                authentication
        );
    }



}
