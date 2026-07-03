package com.optiroute.backend.dto.request;

import java.time.OffsetDateTime;

public record MissionRequest(

        String name,
        Long driverId,
        Long vehicleId,
        Long customerId,

        OffsetDateTime plannedStart,
        OffsetDateTime plannedEnd,

        String originName,
        String originAddress,
        double originLat,
        double originLng,

        String destinationName,
        String destinationAddress,
        double destinationLat,
        double destinationLng) {
}