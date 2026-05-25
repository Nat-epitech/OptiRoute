package com.optiroute.backend.service;

import com.optiroute.backend.dto.request.RouteRequest;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

@Service
public class HereRoutingService {

    private final HereApiClient hereApiClient;

    public HereRoutingService(HereApiClient hereApiClient) {
        this.hereApiClient = hereApiClient;
    }

    public String calculateRoutes(RouteRequest request) {

        String origin = request.getOrigin().getLat() + "," + request.getOrigin().getLng();
        String destination = request.getDestination().getLat() + "," + request.getDestination().getLng();
        String departureTime = request.getDepartureTime() != null ? formatTime(request.getDepartureTime())
                : formatTime(OffsetDateTime.now());

        return hereApiClient.getRoutes(origin, destination, departureTime);
    }

    public String formatTime(OffsetDateTime time) {
        return time.withNano(0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));
    }
}