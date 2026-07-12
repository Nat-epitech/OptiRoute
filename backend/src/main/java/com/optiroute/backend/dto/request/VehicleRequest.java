package com.optiroute.backend.dto.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.JsonNode;

public record VehicleRequest(
        String externalId,
        String externalSource,
        String registration,
        String brand,
        String model,
        String fuelType,
        BigDecimal averageConsumption,
        BigDecimal tankCapacity,
        JsonNode metadata) {
}