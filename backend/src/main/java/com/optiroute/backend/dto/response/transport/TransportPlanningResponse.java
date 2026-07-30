package com.optiroute.backend.dto.response.transport;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransportPlanningResponse(

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