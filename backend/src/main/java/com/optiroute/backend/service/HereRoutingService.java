package com.optiroute.backend.service;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;

import com.optiroute.backend.client.HereApiClient;
import com.optiroute.backend.dto.request.RouteRequest;
import com.optiroute.backend.model.TruckConfiguration;
import com.optiroute.backend.utils.CommonUtils;

@Service
public class HereRoutingService {

    private final HereApiClient hereApiClient;

    public HereRoutingService(HereApiClient hereApiClient) {
        this.hereApiClient = hereApiClient;
    }

    public String calculateRoutes(RouteRequest request, TruckConfiguration truckConfiguration) {

        String origin = request.getOrigin().getLat() + "," + request.getOrigin().getLng();
        String destination = request.getDestination().getLat() + "," + request.getDestination().getLng();
        String departureTime = request.getDepartureTime() != null ? CommonUtils.formatTime(request.getDepartureTime())
                : CommonUtils.formatTime(OffsetDateTime.now());

        return hereApiClient.getRoutes(origin, destination, departureTime, truckConfiguration);
    }
}