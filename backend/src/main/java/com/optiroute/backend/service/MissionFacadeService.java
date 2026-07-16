package com.optiroute.backend.service;

import com.optiroute.backend.dto.request.MissionRequest;
import com.optiroute.backend.entity.Mission;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optiroute.backend.dto.request.RouteRequest;
import com.optiroute.backend.dto.request.CreateMissionFromRouteRequest;
import com.optiroute.backend.dto.response.RouteResponse;
import com.optiroute.backend.entity.Driver;
import com.optiroute.backend.entity.Vehicle;
import com.optiroute.backend.repository.DriverRepository;
import com.optiroute.backend.repository.VehicleRepository;
import com.optiroute.backend.mapper.RouteRequestFactory;

@Service
public class MissionFacadeService {

    private final MissionService missionService;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    private final RouteRequestFactory routeRequestFactory;
    private final RouteOptimizationService routeOptimizationService;
    private final MissionRouteEstimateService missionRouteEstimateService;

    public MissionFacadeService(
            MissionService missionService,
            DriverRepository driverRepository,
            VehicleRepository vehicleRepository,
            RouteRequestFactory routeRequestFactory,
            RouteOptimizationService routeOptimizationService,
            MissionRouteEstimateService missionRouteEstimateService) {

        this.missionService = missionService;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.routeRequestFactory = routeRequestFactory;
        this.routeOptimizationService = routeOptimizationService;
        this.missionRouteEstimateService = missionRouteEstimateService;
    }

    @Transactional
    public Mission createMission(MissionRequest request) {

        // 1. Sauvegarde de la mission
        Mission mission = missionService.create(request);

        // 2. Chargement des données nécessaires au calcul
        Driver driver = driverRepository.findById(mission.getDriverId()).orElseThrow(() -> new RuntimeException("Driver not found"));

        Vehicle vehicle = vehicleRepository.findById(mission.getVehicleId()).orElseThrow(() -> new RuntimeException("Vehicle not found"));

        // 3. Construction du RouteRequest
        RouteRequest routeRequest = routeRequestFactory.fromMission(mission, vehicle, driver);

        // 4. Calcul de la route
        RouteResponse routeResponse = routeOptimizationService.calculateRoute(routeRequest);

        // 5. Sauvegarde de l'estimation
        missionRouteEstimateService.saveEstimate(mission, routeResponse);

        return mission;
    }

    @Transactional
    public Mission createFromRoute(CreateMissionFromRouteRequest request) {
        Mission mission = missionService.create(request.mission());
        missionRouteEstimateService.saveEstimate(mission, request.selectedRoute(), request.routingProvider(), request.routingMode());

        return mission;
    }
}