package com.optiroute.backend.service;

import com.optiroute.backend.dto.request.MissionRequest;
import com.optiroute.backend.dto.response.MissionResponse;
import com.optiroute.backend.entity.DriverEntity;
import com.optiroute.backend.entity.MissionEntity;
import com.optiroute.backend.entity.RouteEntity;
import com.optiroute.backend.repository.DriverRepository;
import com.optiroute.backend.repository.MissionRepository;
import com.optiroute.backend.mapper.RouteMapper;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final DriverRepository driverRepository;
    private final RouteService routeService;

    public MissionEntity create(MissionRequest request) {

        DriverEntity driver = driverRepository.findById(request.driverId()).orElseThrow(() -> new RuntimeException("Driver not found"));

        if (request.plannedEnd().isBefore(request.plannedStart())) {
            throw new IllegalArgumentException("End datetime must be after start datetime");
        }

        RouteEntity route = routeService.createFromDto(request.selectedRoute(), request.plannedStart());

        MissionEntity mission = new MissionEntity();
        mission.setDriver(driver);
        mission.setName(request.name());
        mission.setPlannedStart(request.plannedStart());
        mission.setPlannedEnd(request.plannedEnd());
        mission.setRoute(route);

        return missionRepository.save(mission);
    }

    public List<MissionResponse> getByDriver(Long driverId) {

        return missionRepository.findByDriverId(driverId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public MissionResponse getById(Long missionId) {

        MissionEntity mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Mission not found"));

        return toResponse(mission);
    }

    private MissionResponse toResponse(MissionEntity mission) {

        MissionResponse response = new MissionResponse();

        response.setId(mission.getId());
        response.setDriverId(mission.getDriver().getId());
        response.setName(mission.getName());
        response.setPlannedStart(mission.getPlannedStart());
        response.setPlannedEnd(mission.getPlannedEnd());
        response.setStatus("PLANNED");

        response.setRoute(RouteMapper.toDto(mission.getRoute()));

        return response;
    }
}