package com.optiroute.backend.dto.response;

import java.util.List;

public class RouteResponse {

    private RouteAlternativeDto selectedRoute;

    private List<RouteAlternativeDto> alternatives;

    public RouteAlternativeDto getSelectedRoute() {
        return selectedRoute;
    }

    public void setSelectedRoute(RouteAlternativeDto selectedRoute) {
        this.selectedRoute = selectedRoute;
    }

    public List<RouteAlternativeDto> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<RouteAlternativeDto> alternatives) {
        this.alternatives = alternatives;
    }
}