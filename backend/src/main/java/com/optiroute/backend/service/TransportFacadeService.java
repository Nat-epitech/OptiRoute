package com.optiroute.backend.service;

import com.optiroute.backend.dto.request.TransportRequest;
import com.optiroute.backend.entity.Transport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optiroute.backend.dto.request.RouteRequest;
import com.optiroute.backend.dto.request.CreateTransportFromRouteRequest;
import com.optiroute.backend.dto.response.RouteResponse;
import com.optiroute.backend.entity.Driver;
import com.optiroute.backend.entity.Vehicle;
import com.optiroute.backend.repository.DriverRepository;
import com.optiroute.backend.repository.VehicleRepository;
import com.optiroute.backend.repository.TransportRepository;

import jakarta.persistence.EntityNotFoundException;

import com.optiroute.backend.mapper.RouteRequestFactory;

@Service
public class TransportFacadeService {

    private final TransportService transportService;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final TransportRepository transportRepository;

    private final RouteRequestFactory routeRequestFactory;
    private final RouteOptimizationService routeOptimizationService;
    private final TransportEstimateService transportEstimateService;

    public TransportFacadeService(
            TransportService transportService,
            DriverRepository driverRepository,
            VehicleRepository vehicleRepository,
            TransportRepository transportRepository,
            RouteRequestFactory routeRequestFactory,
            RouteOptimizationService routeOptimizationService,
            TransportEstimateService transportEstimateService) {

        this.transportService = transportService;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.transportRepository = transportRepository;
        this.routeRequestFactory = routeRequestFactory;
        this.routeOptimizationService = routeOptimizationService;
        this.transportEstimateService = transportEstimateService;
    }

    @Transactional
    public Transport createTransport(TransportRequest request) {

        // 1. Sauvegarde du transport
        Transport transport = transportService.create(request);

        // 2. Chargement des données nécessaires au calcul
        Driver driver = driverRepository.findById(transport.getDriverId()).orElseThrow(() -> new RuntimeException("Driver not found"));

        Vehicle vehicle = vehicleRepository.findById(transport.getVehicleId()).orElseThrow(() -> new RuntimeException("Vehicle not found"));

        // 3. Construction du RouteRequest
        RouteRequest routeRequest = routeRequestFactory.fromTransport(transport, vehicle, driver);

        // 4. Calcul de la route
        RouteResponse routeResponse = routeOptimizationService.calculateRoute(routeRequest);

        // 5. Sauvegarde de l'estimation
        transportEstimateService.saveEstimate(transport, routeResponse);

        return transport;
    }

    @Transactional
    public Transport createFromRoute(CreateTransportFromRouteRequest request) {
        Transport transport = transportService.create(request.transport());
        transportEstimateService.saveEstimate(transport, request.selectedRoute(), request.routingProvider(), request.routingMode());

        return transport;
    }

    @Transactional
    public void deleteTransport(Long id) {
        if (!transportRepository.existsById(id)) {
            throw new EntityNotFoundException("Transport not found with id " + id);
        }

        transportRepository.deleteById(id);
    }
}