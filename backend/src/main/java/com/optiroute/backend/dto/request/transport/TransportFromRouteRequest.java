package com.optiroute.backend.dto.request.transport;

import com.optiroute.backend.dto.response.route.RouteDto;

public record TransportFromRouteRequest(TransportRequest transport, RouteDto selectedRoute) {
}