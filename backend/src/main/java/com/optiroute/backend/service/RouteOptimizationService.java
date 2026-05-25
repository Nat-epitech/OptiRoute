package com.optiroute.backend.service;

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

        // 1. HERE
        String raw = hereRoutingService.calculateRoutes(request);

        // 2. Parsing
        List<HereRouteParser.ParsedRoute> parsedRoutes = hereRouteParser.parseRoutes(raw);

        // 3. Conversion DTO + coûts
        List<RouteAlternativeDto> alternatives = new ArrayList<>();
        double fuelPrice = fuelPriceService.getAverageDieselPrice();
        double consumption = request.getTruck().getFuelConsumptionLitersPer100Km();
        double driverRate = request.getDriverHourlyRate();

        for (HereRouteParser.ParsedRoute parsed : parsedRoutes) {
            double km = parsed.distanceMeters / 1000.0;
            double hours = parsed.durationSeconds / 3600.0;

            RouteCostDetailsDto costs = routeCostService.calculateCosts(km, hours,
                    consumption, fuelPrice,
                    parsed.tollCost, driverRate);

            RouteAlternativeDto dto = new RouteAlternativeDto();
            dto.setDistanceMeters(parsed.distanceMeters);
            dto.setDurationSeconds(parsed.durationSeconds);
            dto.setPolyline(parsed.polyline);
            dto.setCosts(costs);

            alternatives.add(dto);
        }

        // 4. Route FASTEST de référence
        RouteAlternativeDto fastestRoute = alternatives.stream()
                .min(Comparator.comparingLong(RouteAlternativeDto::getDurationSeconds)).orElseThrow();

        long fastestDuration = fastestRoute.getDurationSeconds();

        // 5. Temps max autorisé
        long maxDuration;

        if (request.getMaxTravelTimeMinutes() != null) {
            maxDuration = request.getMaxTravelTimeMinutes() * 60L;
        } else {
            maxDuration = (long) (fastestDuration * 1.10);
        }

        // 6. Filtrage
        List<RouteAlternativeDto> validRoutes = alternatives.stream().filter(r -> r.getDurationSeconds() <= maxDuration)
                .toList();

        // fallback sécurité
        if (validRoutes.isEmpty()) {
            validRoutes = List.of(fastestRoute);
        }

        // 7. Sélection économique
        RouteAlternativeDto cheapestRoute = validRoutes.stream()
                .min(Comparator.comparingDouble(r -> r.getCosts().getTotalCost())).orElseThrow();

        // 8. Réponse
        RouteResponse response = new RouteResponse();

        if (request.getMode().name().equals("FASTEST")) {
            response.setSelectedRoute(fastestRoute);
        } else {
            response.setSelectedRoute(cheapestRoute);
        }

        response.setAlternatives(alternatives);

        return response;
    }
}