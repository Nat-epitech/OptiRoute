package com.optiroute.backend.dto.request.driver;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String phoneNumber;

    private BigDecimal monthlyCost;

    private BigDecimal monthlyWorkingHours;
}