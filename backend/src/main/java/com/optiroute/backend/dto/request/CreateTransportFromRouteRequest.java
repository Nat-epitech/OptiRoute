package com.optiroute.backend.dto.request;

import com.optiroute.backend.dto.dto.RoutesDto;

public record CreateTransportFromRouteRequest(
        TransportRequest transport,
        RoutesDto selectedRoute,
        String routingProvider,
        String routingMode) {
}