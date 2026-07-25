package com.optiroute.backend.dto.response.vehicle;

import com.optiroute.backend.entity.vehicle.TrailerType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record SemiTrailerResponse(
        Long id,
        String externalId,
        String externalSource,

        String registration,

        String brand,
        String model,

        TrailerType trailerType,
        Integer maxSpeed,

        Integer emptyWeightKg,
        Integer grossVehicleWeightKg,

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