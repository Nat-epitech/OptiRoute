package com.optiroute.backend.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import com.optiroute.backend.dto.dto.TransportPlanningDto;
import com.optiroute.backend.entity.Driver;
import com.optiroute.backend.entity.TransportEstimate;
import com.optiroute.backend.repository.DriverRepository;
import com.optiroute.backend.repository.TransportRepository;
import com.optiroute.backend.repository.TransportEstimateRepository;

import org.springframework.stereotype.Service;

@Service
public class TransportPlanningService {

    private final TransportRepository transportRepository;
    private final DriverRepository driverRepository;
    private final TransportEstimateRepository transportEstimateRepository;

    public TransportPlanningService(TransportRepository transportRepository, DriverRepository driverRepository,
            TransportEstimateRepository transportEstimateRepository) {

        this.transportRepository = transportRepository;
        this.driverRepository = driverRepository;
        this.transportEstimateRepository = transportEstimateRepository;
    }

    // Récupére le planning des transports pour une date donnée (1 jour)
    public List<TransportPlanningDto> getPlanning(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
            throw new IllegalArgumentException("endDate must be after startDate");
        }

        OffsetDateTime start = startDate.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime end = endDate.atStartOfDay().atOffset(ZoneOffset.UTC);

        return transportRepository.findByPlannedStartGreaterThanEqualAndPlannedStartLessThan(start, end).stream().map(transport -> {

            Driver driver = driverRepository.findById(transport.getDriverId()).orElseThrow();
            TransportEstimate estimate = transportEstimateRepository.findByTransportId(transport.getId()).orElse(null);

            return new TransportPlanningDto(
                    transport.getId(),
                    transport.getName(),

                    driver.getId(),
                    driver.getFirstName() + " " + driver.getLastName(),

                    transport.getPlannedStart(),
                    transport.getPlannedEnd(),

                    transport.getOriginName(),
                    transport.getDestinationName(),

                    estimate != null ? estimate.getEstimatedTotalCost() : null);
        }).toList();
    }

}