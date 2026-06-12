package com.rm.dto;

import com.rm.entity.Role;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdminCreateUserRequest {

    private String name;
    private String email;
    private String password;
    private Role role;
    // Only for employees
    private BigDecimal salary;

    private BigDecimal dailyWage;
    private Integer monthlyLeaveAllowance;

    private String designation;
}
