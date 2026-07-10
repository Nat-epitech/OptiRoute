package com.optiroute.backend.dto.response;

import java.util.List;

import com.optiroute.backend.dto.dto.RoutesDto;

public class RouteResponse {

    private List<RoutesDto> routes;

    public List<RoutesDto> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RoutesDto> routes) {
        this.routes = routes;
    }
}