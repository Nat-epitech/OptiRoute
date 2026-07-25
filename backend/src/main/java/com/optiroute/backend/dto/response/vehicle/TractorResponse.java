package com.optiroute.backend.dto.response.vehicle;

import com.optiroute.backend.entity.vehicle.FuelType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record TractorResponse(
        Long id,
        String externalId,
        String externalSource,

        String registration,

        String brand,
        String model,

        Integer maxSpeed,

        FuelType fuelType,
        BigDecimal averageConsumption,

        Integer emptyWeightKg,
        Integer grossCombinationWeightKg,

        Integer heightCm,
        Integer widthCm,
        Integer lengthCm,
        Integer axleCount,

        BigDecimal purchaseCost,
        LocalDate depreciationStartDate,
        LocalDate depreciationEndDate,

        Boolean active,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {
}
