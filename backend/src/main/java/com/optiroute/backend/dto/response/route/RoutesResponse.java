package com.optiroute.backend.dto.response.route;

import java.util.List;

public class RoutesResponse {

    private List<RouteDto> routes;

    public List<RouteDto> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteDto> routes) {
        this.routes = routes;
    }
}