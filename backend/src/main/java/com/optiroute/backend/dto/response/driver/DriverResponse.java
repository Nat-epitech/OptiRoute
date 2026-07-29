package com.optiroute.backend.dto.response.driver;

import java.math.BigDecimal;

public record DriverResponse(
        Long id,
        String login,
        String firstName,
        String lastName,
        String phoneNumber,
        BigDecimal monthlyCost,
        BigDecimal monthlyWorkingHours) {
}
