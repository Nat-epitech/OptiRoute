package com.optiroute.backend.service.route;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.optiroute.backend.client.HereApiClient;
import com.optiroute.backend.dto.request.route.RouteRequest;
import com.optiroute.backend.model.TruckConfiguration;
import com.optiroute.backend.type.GpsModeType;
import com.optiroute.backend.utils.CommonUtils;

@Service
public class RoutingService {

    private final HereApiClient hereApiClient;
    private final ObjectMapper objectMapper;

    public RoutingService(HereApiClient hereApiClient, ObjectMapper objectMapper) {
        this.hereApiClient = hereApiClient;
        this.objectMapper = objectMapper;
    }

    public String calculateRoutes(RouteRequest request, TruckConfiguration truckConfiguration) {

        List<String> viaPoints = request.getWaypoints() == null ? List.of()
            : request.getWaypoints().stream().filter(point -> point != null).map(point -> point.getLat() + "," + point.getLng()).toList();

        if (viaPoints.size() > 4) {
            throw new IllegalArgumentException("Un itinéraire ne peut contenir que 4 étapes maximum.");
        }

        String origin = request.getOrigin().getLat() + "," + request.getOrigin().getLng();
        String destination = request.getDestination().getLat() + "," + request.getDestination().getLng();
        String departureTime = request.getDepartureTime() != null ? CommonUtils.formatTime(request.getDepartureTime()) : CommonUtils.formatTime(OffsetDateTime.now());

        if (GpsModeType.FASTEST.equals(request.getMode())) {
            String routeWithTolls = hereApiClient.getRoutes(origin,destination,viaPoints,departureTime,truckConfiguration,false,0);
            String routeWithoutTolls = hereApiClient.getRoutes(origin,destination,viaPoints,departureTime,truckConfiguration,true,0);

            return mergeRoutes(routeWithTolls,routeWithoutTolls);
        }

        if (GpsModeType.CHEAPEST.equals(request.getMode())) {
            String routesWithTolls = hereApiClient.getRoutes(origin,destination,viaPoints,departureTime,truckConfiguration,false,3);
            String routesWithoutTolls = hereApiClient.getRoutes(origin,destination,viaPoints,departureTime,truckConfiguration,true,3);

            return mergeRoutes(routesWithTolls,routesWithoutTolls);
        }

        return hereApiClient.getRoutes(origin,destination,viaPoints,departureTime,truckConfiguration,false,2);
    }

    private String mergeRoutes(String firstResponse, String secondResponse) {
        try {
            JsonNode firstRoot = objectMapper.readTree(firstResponse);
            JsonNode secondRoot = objectMapper.readTree(secondResponse);

            ObjectNode mergedRoot = objectMapper.createObjectNode();
            ArrayNode mergedRoutes = mergedRoot.putArray("routes");
            appendRoutes(mergedRoutes,firstRoot.path("routes"));
            appendRoutes(mergedRoutes,secondRoot.path("routes"));

            return objectMapper.writeValueAsString(mergedRoot);
        } catch (Exception e) {
            throw new RuntimeException("HERE responses merging failed", e);
        }
    }

    private void appendRoutes(ArrayNode target, JsonNode routes) {
        if (routes.isArray()) {
            routes.forEach(target::add);
        }
    }
}