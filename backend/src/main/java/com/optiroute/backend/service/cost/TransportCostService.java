package com.optiroute.backend.service.cost;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.optiroute.backend.dto.response.cost.TransportCostDetailsResponse;
import com.optiroute.backend.dto.response.cost.VehicleCostDetailsResponse;
import com.optiroute.backend.dto.response.cost.AppliedCostResponse;
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

@Service
@RequiredArgsConstructor
public class TransportCostService {

    private static final ZoneId PLANNING_ZONE = ZoneId.of("Europe/Paris");

    private static final String FUEL_LABEL = "Carburant";
    private static final String TOLL_LABEL = "Péages";

    private final TransportEstimateRepository transportEstimateRepository;
    private final TransportRepository transportRepository;
    private final TractorService tractorService;
    private final SemiTrailerService semiTrailerService;
    private final VehicleCostService vehicleCostService;

    public TransportCostDetailsResponse calculateCosts(Transport transport, TransportEstimate estimate) {

        if (estimate == null) {
            return null;
        }

        List<AppliedCostResponse> vehicleCosts = new ArrayList<>();

        // Fuel
        double fuelCost = estimate.getEstimatedFuelCost().doubleValue();
        vehicleCosts.add(new AppliedCostResponse(FUEL_LABEL, fuelCost));

        // Toll
        double tollCost = estimate.getEstimatedTollCost().doubleValue();
        vehicleCosts.add(new AppliedCostResponse(TOLL_LABEL, tollCost));

        double distanceKm = estimate.getDistanceMeters() / 1000.0;
        LocalDate transportDate = transport.getPlannedStart().atZoneSameInstant(PLANNING_ZONE).toLocalDate();
        double dailyVehicleDistanceKm = calculateDailyVehicleDistance(transport.getTractorId(),transportDate);

        Tractor tractor = tractorService.getEntityById(transport.getTractorId());
        SemiTrailer semiTrailer = semiTrailerService.getEntityById(transport.getSemiTrailerId());

        VehicleCostDetailsResponse vehicleCostDetails = vehicleCostService.calculateCosts(tractor,semiTrailer,distanceKm,dailyVehicleDistanceKm,transportDate);
        vehicleCosts.addAll(vehicleCostDetails.costs());

        double vehicleTotal = vehicleCosts.stream().mapToDouble(AppliedCostResponse::amount).sum();

        CostCategoryResponse vehicle = new CostCategoryResponse(vehicleCosts, vehicleTotal);
        CostCategoryResponse driver = new CostCategoryResponse(List.of(), 0);
        CostCategoryResponse structure = new CostCategoryResponse(List.of(), 0);

        double totalCost = vehicle.totalCost() + driver.totalCost() + structure.totalCost();

        return new TransportCostDetailsResponse(vehicle, driver, structure, totalCost);
    }

    private double calculateDailyVehicleDistance(Long tractorId, LocalDate date) {

        OffsetDateTime start = date.atStartOfDay(PLANNING_ZONE).toOffsetDateTime();
        OffsetDateTime end = date.plusDays(1).atStartOfDay(PLANNING_ZONE).toOffsetDateTime();

        return transportRepository.findByTractorIdAndPlannedStartGreaterThanEqualAndPlannedStartLessThan(tractorId,start,end).stream().map(Transport::getId)
            .map(transportEstimateRepository::findByTransportId).flatMap(Optional::stream).mapToDouble(transportEstimate -> transportEstimate.getDistanceMeters() / 1000.0).sum();
    }
}