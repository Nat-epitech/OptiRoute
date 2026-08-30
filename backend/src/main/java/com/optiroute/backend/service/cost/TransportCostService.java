package com.optiroute.backend.service.cost;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.optiroute.backend.dto.response.cost.TransportCostDetailsResponse;
import com.optiroute.backend.dto.request.cost.CostCalculationContext;
import com.optiroute.backend.dto.response.cost.CostCategoryResponse;

import com.optiroute.backend.entity.transport.Transport;
import com.optiroute.backend.entity.transport.TransportEstimate;
import com.optiroute.backend.entity.vehicle.SemiTrailer;
import com.optiroute.backend.entity.vehicle.Tractor;

import com.optiroute.backend.repository.transport.TransportEstimateRepository;
import com.optiroute.backend.repository.transport.TransportRepository;

import com.optiroute.backend.service.vehicle.SemiTrailerService;
import com.optiroute.backend.service.vehicle.TractorService;

import lombok.RequiredArgsConstructor;
import com.optiroute.backend.entity.driver.Driver;
import com.optiroute.backend.repository.driver.DriverRepository;

@Service
@RequiredArgsConstructor
public class TransportCostService {

    private static final ZoneId PLANNING_ZONE = ZoneId.of("Europe/Paris");

    private final DriverRepository driverRepository;
    private final TransportRepository transportRepository;
    private final TransportEstimateRepository transportEstimateRepository;

    private final TractorService tractorService;
    private final SemiTrailerService semiTrailerService;

    private final VehicleCostService vehicleCostService;
    private final DriverCostService driverCostService;
    private final StructureCostService structureCostService;

    public TransportCostDetailsResponse calculateCosts(Transport transport, TransportEstimate estimate) {
        double distanceKm = estimate.getDistanceMeters() / 1000.0;
        double durationHours = estimate.getDurationSeconds() / 3600.0;

        LocalDate transportDate = transport.getPlannedStart().atZoneSameInstant(PLANNING_ZONE).toLocalDate();
        LocalTime departureTime = transport.getPlannedStart().atZoneSameInstant(PLANNING_ZONE).toLocalTime();
        LocalTime arrivalTime = transport.getPlannedEnd().atZoneSameInstant(PLANNING_ZONE).toLocalTime();

        double dailyVehicleDistanceKm = calculateDailyVehicleDistance(transport.getTractorId(),transportDate);
        int dailyTransportCount = calculateDailyTransportCount(transportDate);

        double dailyDriverDurationHours = calculateDailyDriverDuration(transport.getDriverId(),transportDate);
        LocalTime driverDayStartTime = calculateDailyDriverStartTime(transport.getDriverId(),transportDate);
        LocalTime driverDayEndTime = calculateDailyDriverEndTime(transport.getDriverId(),transportDate);

        Tractor tractor = tractorService.getEntityById(transport.getTractorId());
        SemiTrailer semiTrailer = transport.getSemiTrailerId() != null ? semiTrailerService.getEntityById(transport.getSemiTrailerId()) : null;
        String vehicleType = semiTrailer != null && semiTrailer.getTrailerType() != null ? semiTrailer.getTrailerType().name() : null;

        CostCalculationContext context = new CostCalculationContext(transportDate, distanceKm, dailyVehicleDistanceKm, durationHours, dailyTransportCount, departureTime,
            arrivalTime, driverDayStartTime, driverDayEndTime, vehicleType, dailyDriverDurationHours);

        CostCategoryResponse vehicleCost = vehicleCostService.calculateCosts(tractor,semiTrailer,context,estimate.getEstimatedFuelCost().doubleValue(),
            estimate.getEstimatedTollCost().doubleValue());

        Driver driver = driverRepository.findById(transport.getDriverId()).orElseThrow(() -> new RuntimeException("Driver not found"));
        CostCategoryResponse driverCost = driverCostService.calculateCosts(driver,context);

        CostCategoryResponse structureCost = structureCostService.calculateCosts(context);

        double totalCost = vehicleCost.totalCost() + driverCost.totalCost() + structureCost.totalCost();

        return new TransportCostDetailsResponse(vehicleCost, driverCost, structureCost, totalCost);
    }

    private double calculateDailyVehicleDistance(Long tractorId, LocalDate date) {

        OffsetDateTime start = date.atStartOfDay(PLANNING_ZONE).toOffsetDateTime();
        OffsetDateTime end = date.plusDays(1).atStartOfDay(PLANNING_ZONE).toOffsetDateTime();

        return transportRepository.findByTractorIdAndPlannedStartGreaterThanEqualAndPlannedStartLessThan(tractorId,start,end).stream().map(Transport::getId)
            .map(transportEstimateRepository::findByTransportId).flatMap(Optional::stream).mapToDouble(transportEstimate -> transportEstimate.getDistanceMeters() / 1000.0).sum();
    }

    private int calculateDailyTransportCount(LocalDate date) {
        OffsetDateTime start = date.atStartOfDay(PLANNING_ZONE).toOffsetDateTime();
        OffsetDateTime end = date.plusDays(1).atStartOfDay(PLANNING_ZONE).toOffsetDateTime();

        return transportRepository.findByPlannedStartGreaterThanEqualAndPlannedStartLessThan(start,end).size();
    }

    private double calculateDailyDriverDuration(Long driverId, LocalDate date) {

        OffsetDateTime start = date.atStartOfDay(PLANNING_ZONE).toOffsetDateTime();

        OffsetDateTime end = date.plusDays(1).atStartOfDay(PLANNING_ZONE).toOffsetDateTime();

        return transportRepository.findByDriverIdAndPlannedStartGreaterThanEqualAndPlannedStartLessThan(driverId,start,end).stream().map(Transport::getId)
            .map(transportEstimateRepository::findByTransportId).flatMap(Optional::stream).mapToDouble(estimate -> estimate.getDurationSeconds() / 3600.0).sum();
    }

    private LocalTime calculateDailyDriverStartTime(Long driverId, LocalDate date) {
        OffsetDateTime start = date.atStartOfDay(PLANNING_ZONE).toOffsetDateTime();
        OffsetDateTime end = date.plusDays(1).atStartOfDay(PLANNING_ZONE).toOffsetDateTime();

        return transportRepository.findByDriverIdAndPlannedStartGreaterThanEqualAndPlannedStartLessThan(driverId,start,end).stream()
            .map(transport -> transport.getPlannedStart().atZoneSameInstant(PLANNING_ZONE).toLocalTime()).min(LocalTime::compareTo).orElse(null);
    }

    private LocalTime calculateDailyDriverEndTime(Long driverId, LocalDate date) {
        OffsetDateTime start = date.atStartOfDay(PLANNING_ZONE).toOffsetDateTime();
        OffsetDateTime end = date.plusDays(1).atStartOfDay(PLANNING_ZONE).toOffsetDateTime();

        return transportRepository.findByDriverIdAndPlannedStartGreaterThanEqualAndPlannedStartLessThan(driverId,start,end).stream()
            .map(transport -> transport.getPlannedEnd().atZoneSameInstant(PLANNING_ZONE).toLocalTime()).max(LocalTime::compareTo).orElse(null);
    }
}