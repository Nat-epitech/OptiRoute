package com.optiroute.backend.dto.request.driver;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record DriverRequest(@NotBlank String firstName, @NotBlank String lastName, String phoneNumber, @NotNull @PositiveOrZero BigDecimal annualSalary,
        @NotNull @Positive BigDecimal monthlyWorkingHours) {
}