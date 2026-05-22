package com.optiroute.backend.service;

import com.optiroute.backend.dto.request.RouteRequest;
import com.optiroute.backend.dto.response.*;
import org.springframework.stereotype.Service;

@Service
public class RouteOptimizationService {

    private final HereRoutingService hereRoutingService;
    private final HereRouteParser hereRouteParser;
    private final RouteCostService routeCostService;

    public RouteOptimizationService(
            HereRoutingService hereRoutingService,
            HereRouteParser hereRouteParser,
            RouteCostService routeCostService) {
        this.hereRoutingService = hereRoutingService;
        this.hereRouteParser = hereRouteParser;
        this.routeCostService = routeCostService;
    }

    public RouteResponse calculateRoute(RouteRequest request) {

        // 1. appel HERE
        String raw = hereRoutingService.calculateFastestRoute(request);

        // 2. parsing
        HereRouteParser.ParsedRoute parsed = hereRouteParser.parse(raw);

        // 3. calcul coûts
        double km = parsed.distanceMeters / 1000.0;
        double hours = parsed.durationSeconds / 3600.0;

        var costs = routeCostService.calculateCosts(
                km,
                hours,
                request.getTruck().getFuelConsumptionLitersPer100Km(),
                request.getFuelPricePerLiter(),
                0.0, // toll placeholder pour V1
                request.getDriverHourlyRate());

        // 4. response
        RouteAlternativeDto route = new RouteAlternativeDto();
        route.setDistanceMeters(parsed.distanceMeters);
        route.setDurationSeconds(parsed.durationSeconds);
        route.setPolyline(parsed.polyline);
        route.setCosts(costs);

        RouteResponse response = new RouteResponse();
        response.setSelectedRoute(route);

        return response;
    }
}