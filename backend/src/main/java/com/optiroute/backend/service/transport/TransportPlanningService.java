package com.optiroute.backend.service.transport;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

import com.optiroute.backend.dto.response.cost.TransportCostDetailsResponse;
import com.optiroute.backend.dto.response.transport.TransportPlanningResponse;
import com.optiroute.backend.entity.driver.Driver;
import com.optiroute.backend.entity.transport.TransportEstimate;
import com.optiroute.backend.repository.driver.DriverRepository;
import com.optiroute.backend.repository.transport.TransportEstimateRepository;
import com.optiroute.backend.repository.transport.TransportRepository;
import com.optiroute.backend.service.cost.TransportCostService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransportPlanningService {

    private final TransportRepository transportRepository;
    private final DriverRepository driverRepository;
    private final TransportEstimateRepository transportEstimateRepository;
    private final TransportCostService transportCostService;

    // Récupére le planning des transports pour une plage de date
    private static final ZoneId PLANNING_ZONE = ZoneId.of("Europe/Paris");

    public List<TransportPlanningResponse> getPlanning(LocalDate startDate, LocalDate endDate) {

        if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
            throw new IllegalArgumentException("endDate must be after startDate");
        }

        OffsetDateTime start = startDate.atStartOfDay(PLANNING_ZONE).toOffsetDateTime();
        OffsetDateTime end = endDate.atStartOfDay(PLANNING_ZONE).toOffsetDateTime();

        return transportRepository.findByPlannedStartGreaterThanEqualAndPlannedStartLessThan(start,end).stream().map(transport -> {
            Driver driver = driverRepository.findById(transport.getDriverId()).orElseThrow();
            TransportEstimate estimate = transportEstimateRepository.findByTransportId(transport.getId()).orElse(null);
            TransportCostDetailsResponse costs = transportCostService.calculateCosts(transport,estimate);

            return new TransportPlanningResponse(transport.getId(), transport.getName(), driver.getId(), driver.getFirstName() + " " + driver.getLastName(),
                transport.getPlannedStart(), transport.getPlannedEnd(), transport.getOriginName(), transport.getDestinationName(), transport.isEmptyTrip(), costs.totalCost());
        }).toList();
    }

}