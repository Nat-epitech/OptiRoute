package com.optiroute.backend.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DriverResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private BigDecimal monthlySalary;
    private BigDecimal monthlyWorkingHours;
}