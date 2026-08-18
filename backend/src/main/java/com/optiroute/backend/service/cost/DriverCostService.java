package com.optiroute.backend.service.cost;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.optiroute.backend.dto.response.cost.AppliedCostResponse;
import com.optiroute.backend.dto.response.cost.CostCategoryResponse;
import com.optiroute.backend.entity.transport.TransportEstimate;
import com.optiroute.backend.entity.transport.Transport;
import com.optiroute.backend.entity.driver.Driver;
import com.optiroute.backend.service.driver.DriverService;
import com.optiroute.backend.repository.transport.TransportEstimateRepository;
import com.optiroute.backend.repository.transport.TransportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverCostService {

    private static final ZoneId PLANNING_ZONE = ZoneId.of("Europe/Paris");

    private final WorkingDaysService workingDaysService;
    private final TransportRepository transportRepository;
    private final TransportEstimateRepository transportEstimateRepository;
    private final DriverService driverService;

    public CostCategoryResponse calculateCosts(Transport transport, TransportEstimate estimate) {

        if (estimate == null || transport.getDriverId() == null) {
            return new CostCategoryResponse(List.of(), 0);
        }

        LocalDate date = transport.getPlannedStart().atZoneSameInstant(PLANNING_ZONE).toLocalDate();

        double durationHours = estimate.getDurationSeconds() / 3600.0;

        double dailyDriverDurationHours = calculateDailyDriverDuration(transport.getDriverId(),date);

        if (dailyDriverDurationHours <= 0) {
            return new CostCategoryResponse(List.of(), 0);
        }

        Driver driver = driverService.getEntityById(transport.getDriverId());

        int workingDaysYear = workingDaysService.getWorkingDaysInYear(date.getYear());

        if (workingDaysYear <= 0) {
            return new CostCategoryResponse(List.of(), 0);
        }

        BigDecimal annualSalary = driver.getAnnualSalary();

        if (annualSalary == null) {
            return new CostCategoryResponse(List.of(), 0);
        }

        double dailySalary = annualSalary.doubleValue() / workingDaysYear;

        double transportSalary = dailySalary * (durationHours / dailyDriverDurationHours);

        AppliedCostResponse cost = new AppliedCostResponse("Salaire", transportSalary);

        return new CostCategoryResponse(List.of(cost), transportSalary);
    }

    private double calculateDailyDriverDuration(Long driverId, LocalDate date) {

        OffsetDateTime start = date.atStartOfDay(PLANNING_ZONE).toOffsetDateTime();

        OffsetDateTime end = date.plusDays(1).atStartOfDay(PLANNING_ZONE).toOffsetDateTime();

        return transportRepository.findByDriverIdAndPlannedStartGreaterThanEqualAndPlannedStartLessThan(driverId,start,end).stream().map(Transport::getId)
            .map(transportEstimateRepository::findByTransportId).flatMap(Optional::stream).mapToDouble(estimate -> estimate.getDurationSeconds() / 3600.0).sum();
    }
}