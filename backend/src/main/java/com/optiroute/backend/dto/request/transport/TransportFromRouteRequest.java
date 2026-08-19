package com.optiroute.backend.dto.request.transport;

import com.optiroute.backend.dto.response.route.RoutesDto;

public record TransportFromRouteRequest(TransportRequest transport, RoutesDto selectedRoute) {
}