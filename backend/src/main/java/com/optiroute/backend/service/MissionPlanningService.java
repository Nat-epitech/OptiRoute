package com.optiroute.backend.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import com.optiroute.backend.dto.dto.MissionPlanningDto;
import com.optiroute.backend.entity.Driver;
import com.optiroute.backend.entity.MissionRouteEstimate;
import com.optiroute.backend.repository.DriverRepository;
import com.optiroute.backend.repository.MissionRepository;
import com.optiroute.backend.repository.MissionRouteEstimateRepository;

import org.springframework.stereotype.Service;

@Service
public class MissionPlanningService {

    private final MissionRepository missionRepository;
    private final DriverRepository driverRepository;
    private final MissionRouteEstimateRepository estimateRepository;

    public MissionPlanningService(MissionRepository missionRepository, DriverRepository driverRepository,
            MissionRouteEstimateRepository estimateRepository) {

        this.missionRepository = missionRepository;
        this.driverRepository = driverRepository;
        this.estimateRepository = estimateRepository;
    }

    // Récupére le planning des missions pour une date donnée (1 jour)
    public List<MissionPlanningDto> getPlanning(LocalDate date) {

        OffsetDateTime start = date.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime end = start.plusDays(1);

        return missionRepository.findByPlannedStartBetween(start, end).stream().map(mission -> {

            Driver driver = driverRepository.findById(mission.getDriverId()).orElseThrow();
            MissionRouteEstimate estimate = estimateRepository.findByMissionId(mission.getId()).orElse(null);

            return new MissionPlanningDto(
                    mission.getId(),
                    mission.getName(),

                    driver.getId(),
                    driver.getFirstName() + " " + driver.getLastName(),

                    mission.getPlannedStart(),
                    mission.getPlannedEnd(),

                    mission.getOriginName(),
                    mission.getDestinationName(),

                    estimate != null ? estimate.getEstimatedTotalCost() : null);
        }).toList();
    }

}