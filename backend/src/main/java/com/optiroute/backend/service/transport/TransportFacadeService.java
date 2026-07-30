package com.optiroute.backend.service.transport;

import com.optiroute.backend.dto.response.route.RouteResponse;
import com.optiroute.backend.entity.Transport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optiroute.backend.dto.request.planning.CreateTransportFromRouteRequest;
import com.optiroute.backend.dto.request.planning.TransportRequest;
import com.optiroute.backend.dto.request.route.RouteRequest;
import com.optiroute.backend.repository.TransportRepository;
import com.optiroute.backend.service.route.RouteOptimizationService;

import jakarta.persistence.EntityNotFoundException;

import com.optiroute.backend.mapper.RouteRequestFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransportFacadeService {

    private final TransportService transportService;
    private final TransportRepository transportRepository;

    private final RouteRequestFactory routeRequestFactory;
    private final RouteOptimizationService routeOptimizationService;
    private final TransportEstimateService transportEstimateService;

    @Transactional
    public Transport createTransport(TransportRequest request) {

        // Sauvegarde du transport
        Transport transport = transportService.create(request);

        // Construction du RouteRequest
        RouteRequest routeRequest = routeRequestFactory.fromTransport(transport);

        // Calcul de la route
        RouteResponse routeResponse = routeOptimizationService.calculateRoute(routeRequest);

        // Sauvegarde de l'estimation
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