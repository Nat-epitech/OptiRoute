package com.optiroute.backend.dto.response.route;

import java.util.List;

public class RouteResponse {

    private List<RoutesDto> routes;

    public List<RoutesDto> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RoutesDto> routes) {
        this.routes = routes;
    }
}