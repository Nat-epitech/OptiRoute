package com.optiroute.backend.dto.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.databind.JsonNode;

public record VehicleDto(
        Long id,
        String externalId,
        String externalSource,
        String registration,
        String brand,
        String model,
        String fuelType,
        BigDecimal averageConsumption,
        BigDecimal tankCapacity,
        JsonNode metadata,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {
}