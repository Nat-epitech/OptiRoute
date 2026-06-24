package com.optiroute.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optiroute.backend.client.dto.RoutesDto;
import com.optiroute.backend.entity.RouteEntity;
import com.optiroute.backend.repository.RouteRepository;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;

@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public RouteEntity createFromDto(RoutesDto dto, OffsetDateTime departureTime) {

        try {
            RouteEntity route = new RouteEntity();

            route.setDistanceKm(dto.getDistanceMeters() / 1000.0);
            route.setDurationMinutes(dto.getDurationSeconds() / 60.0);

            route.setFuelCost(dto.getCosts().getFuelCost());
            route.setTollCost(dto.getCosts().getTollCost());
            route.setDriverCost(dto.getCosts().getDriverCost());
            route.setFuelCost(dto.getCosts().getFuelCost());
            route.setTollCost(dto.getCosts().getTollCost());

            route.setPolyline(dto.getPolyline());

            route.setActions(objectMapper.writeValueAsString(dto.getActions()));

            route.setHereData(dto.getRawHereData());

            route.setDepartureTime(departureTime);

            route.setArrivalTime(
                    departureTime.plusSeconds(dto.getDurationSeconds()));

            route.setCreatedAt(OffsetDateTime.now());

            return routeRepository.save(route);

        } catch (Exception e) {
            throw new RuntimeException("Route creation failed", e);
        }
    }
}