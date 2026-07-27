package com.optiroute.backend.dto.request.planning;

import com.optiroute.backend.dto.response.route.RoutesDto;

public record CreateTransportFromRouteRequest(
        TransportRequest transport,
        RoutesDto selectedRoute,
        String routingProvider,
        String routingMode) {
}