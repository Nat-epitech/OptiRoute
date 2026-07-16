package com.optiroute.backend.dto.request;

import com.optiroute.backend.dto.dto.RoutesDto;

public record CreateMissionFromRouteRequest(
        MissionRequest mission,
        RoutesDto selectedRoute,
        String routingProvider,
        String routingMode
) {
}