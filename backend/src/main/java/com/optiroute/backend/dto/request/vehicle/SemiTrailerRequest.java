package com.optiroute.backend.dto.request.vehicle;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.optiroute.backend.type.TrailerType;

public record SemiTrailerRequest(
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
