package com.optiroute.backend.service;

import com.optiroute.backend.client.dto.RoutesDto;
import com.optiroute.backend.client.dto.RouteCostDetailsDto;
import com.optiroute.backend.dto.request.RouteRequest;
import com.optiroute.backend.dto.response.*;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RouteOptimizationService {

    private final HereRoutingService hereRoutingService;
    private final HereRouteParser hereRouteParser;
    private final RouteCostService routeCostService;
    private final FuelPriceService fuelPriceService;

    public RouteOptimizationService(HereRoutingService hereRoutingService, HereRouteParser hereRouteParser,
            RouteCostService routeCostService, FuelPriceService fuelPriceService) {
        this.hereRoutingService = hereRoutingService;
        this.hereRouteParser = hereRouteParser;
        this.routeCostService = routeCostService;
        this.fuelPriceService = fuelPriceService;
    }

    public RouteResponse calculateRoute(RouteRequest request) {

        // HERE Routing API + Parsing
        String raw = hereRoutingService.calculateRoutes(request);
        List<HereRouteParser.ParsedRoute> parsedRoutes = hereRouteParser.parseRoutes(raw);

        // Cost calculation
        double fuelPrice = fuelPriceService.getAverageDieselPrice();
        double consumption = request.getTruck().getFuelConsumptionLitersPer100Km();
        double driverRate = request.getDriverHourlyRate();

        // Enriched DTOs
        List<RoutesDto> routes = new ArrayList<>();
        for (HereRouteParser.ParsedRoute parsed : parsedRoutes) {
            double km = parsed.distanceMeters / 1000.0;
            double hours = parsed.durationSeconds / 3600.0;
            RouteCostDetailsDto costs = routeCostService.calculateCosts(km, hours, consumption, fuelPrice, parsed.tollCost, driverRate);

            RoutesDto dto = new RoutesDto();
            dto.setDistanceMeters(parsed.distanceMeters);
            dto.setDurationSeconds(parsed.durationSeconds);
            dto.setPolyline(parsed.polyline);
            dto.setCosts(costs);

            dto.setOriginLat(request.getOrigin().getLat());
            dto.setOriginLng(request.getOrigin().getLng());
            dto.setDestinationLat(request.getDestination().getLat());
            dto.setDestinationLng(request.getDestination().getLng());
            // dto.setRawHereData(parsed.rawJson);

            routes.add(dto);
        }

        // Fastest route reference
        RoutesDto fastestRoute = routes.stream().min(Comparator.comparingLong(RoutesDto::getDurationSeconds)).orElseThrow();
        long fastestDuration = fastestRoute.getDurationSeconds();

        // Max duration management
        long maxDuration = (request.getMaxTravelTimeMinutes() != null)
                ? request.getMaxTravelTimeMinutes() * 60L
                : (long) (fastestDuration * 1.10);

        // Filtrage
        List<RoutesDto> validRoutes = routes.stream().filter(r -> r.getDurationSeconds() <= maxDuration).toList();
        if (validRoutes.isEmpty()) {
            validRoutes = List.of(fastestRoute); // fallback
        }

        // Response
        RouteResponse response = new RouteResponse();
        response.setRoutes(validRoutes);

        return response;
    }
}