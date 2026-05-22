package com.optiroute.backend.service;

import com.optiroute.backend.dto.request.RouteRequest;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

@Service
public class HereRoutingService {

    private final HereApiClient hereApiClient;

    public HereRoutingService(HereApiClient hereApiClient) {
        this.hereApiClient = hereApiClient;
    }

    public String calculateFastestRoute(RouteRequest request) {

        String origin = request.getOrigin().getLat() + "," + request.getOrigin().getLng();
        String destination = request.getDestination().getLat() + "," + request.getDestination().getLng();

        String departureTime = request.getDepartureTime() != null
                ? request.getDepartureTime()
                        .withNano(0)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"))
                : null;

        return hereApiClient.getFastestRoute(origin, destination, departureTime);
    }
}