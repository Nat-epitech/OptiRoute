package com.optiroute.backend.dto.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransportPlanningDto(

        Long id,
        String name,

        Long driverId,
        String driverName,

        OffsetDateTime plannedStart,
        OffsetDateTime plannedEnd,

        String originName,
        String destinationName,

        BigDecimal estimatedTotalCost) {
}