package com.optiroute.backend.service.route;

import com.optiroute.backend.dto.request.route.RouteRequest;
import com.optiroute.backend.dto.response.route.RouteCostDetailsDto;
import com.optiroute.backend.dto.response.route.RoutesResponse;
import com.optiroute.backend.dto.response.route.RouteDto;
import com.optiroute.backend.entity.vehicle.SemiTrailer;
import com.optiroute.backend.entity.vehicle.Tractor;
import com.optiroute.backend.service.cost.FuelPriceService;
import com.optiroute.backend.service.vehicle.SemiTrailerService;
import com.optiroute.backend.service.vehicle.TractorService;

import lombok.RequiredArgsConstructor;

import com.optiroute.backend.model.TruckConfiguration;
import com.optiroute.backend.mapper.TruckConfigurationFactory;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.optiroute.backend.type.GpsModeType;

@Service
@RequiredArgsConstructor
public class RouteOptimizationService {

    private final RoutingService hereRoutingService;
    private final RouteHereParser hereRouteParser;
    private final RouteCostService routeCostService;
    private final FuelPriceService fuelPriceService;
    private final TractorService tractorService;
    private final SemiTrailerService semiTrailerService;
    private final TruckConfigurationFactory truckConfigurationFactory;

    public RoutesResponse calculateRoute(RouteRequest request) {

        // Get Truck Configuration
        Tractor tractor = tractorService.getEntityById(request.getTractorId());
        SemiTrailer semiTrailer = semiTrailerService.getEntityById(request.getSemiTrailerId());
        TruckConfiguration truckConfiguration = truckConfigurationFactory.create(tractor,semiTrailer,request.isEmptyTrip());

        // HERE Routing API + Parsing
        String raw = hereRoutingService.calculateRoutes(request,truckConfiguration);
        List<RouteHereParser.ParsedRoute> parsedRoutes = hereRouteParser.parseRoutes(raw);

        // Cost calculation
        double fuelPrice = fuelPriceService.getAverageDieselPrice();
        double consumption = truckConfiguration.getAverageConsumption().doubleValue();

        // Enriched DTOs
        List<RouteDto> routes = new ArrayList<>();
        for (RouteHereParser.ParsedRoute parsed : parsedRoutes) {
            double km = parsed.distanceMeters / 1000.0;
            RouteCostDetailsDto costs = routeCostService.calculateCosts(km,consumption,fuelPrice,parsed.tollCost);

            RouteDto dto = new RouteDto();
            dto.setDistanceMeters(parsed.distanceMeters);
            dto.setDuration(parsed.duration);
            dto.setBaseDuration(parsed.baseDuration);
            dto.setPolyline(parsed.polyline);
            dto.setCosts(costs);

            dto.setOriginLat(request.getOrigin().getLat());
            dto.setOriginLng(request.getOrigin().getLng());
            dto.setDestinationLat(request.getDestination().getLat());
            dto.setDestinationLng(request.getDestination().getLng());

            routes.add(dto);
        }

        List<RouteDto> validRoutes = routes;

        if (GpsModeType.CHEAPEST.equals(request.getMode())) {
            long maxDurationSeconds = request.getMaxTravelTimeMinutes() == null ? Long.MAX_VALUE : request.getMaxTravelTimeMinutes() * 60L;

            List<RouteDto> sortedRoutes = routes.stream().sorted(Comparator.comparingDouble(route -> route.getCosts().getTotalCost())).toList();

            validRoutes = sortedRoutes.stream().filter(route -> route.getDuration() <= maxDurationSeconds).toList();

            if (validRoutes.isEmpty() && !sortedRoutes.isEmpty()) {
                validRoutes = List.of(sortedRoutes.getFirst());
            } else {
                validRoutes = validRoutes.stream().limit(3).toList();
            }
        }

        // Response
        RoutesResponse response = new RoutesResponse();
        response.setRoutes(validRoutes);

        return response;
    }
}