package com.optiroute.backend.dto.request.cost;

import java.time.LocalDate;
import java.time.LocalTime;

public record CostCalculationContext(LocalDate date,

    double distanceKm, double dailyVehicleDistanceKm,

    double durationHours,

    int dailyTransportCount,

    LocalTime departureTime,

    LocalTime arrivalTime,

    LocalTime driverDayStartTime,

    LocalTime driverDayEndTime,

    String vehicleType,

    double dailyDriverDurationHours) {
}