package com.optiroute.backend.dto.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record MissionDetailDto(

        Long id,
        String name,
        String status,

        OffsetDateTime plannedStart,
        OffsetDateTime plannedEnd,

        OffsetDateTime actualStart,
        OffsetDateTime actualEnd,

        // Driver
        Long driverId,
        String driverName,
        String driverEmail,

        // Vehicle
        Long vehicleId,
        String vehicleRegistration,
        String vehicleBrand,
        String vehicleModel,

        // Customer
        Long customerId,
        String customerName,
        String customerAddress,
        String customerCity,

        // Origin
        String originName,
        String originAddress,
        double originLat,
        double originLng,

        // Destination
        String destinationName,
        String destinationAddress,
        double destinationLat,
        double destinationLng,

        // Route estimate
        Long distanceMeters,
        Long durationSeconds,
        Long trafficDurationSeconds,

        String polyline,

        BigDecimal estimatedFuelCost,
        BigDecimal estimatedTollCost,
        BigDecimal estimatedDriverCost,
        BigDecimal estimatedTotalCost

) {
}
