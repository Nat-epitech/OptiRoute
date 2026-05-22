package com.optiroute.backend.controller;

import com.optiroute.backend.dto.request.RouteRequest;
import com.optiroute.backend.dto.response.RouteResponse;
import com.optiroute.backend.service.RouteOptimizationService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteOptimizationService routeOptimizationService;

    public RouteController(RouteOptimizationService routeOptimizationService) {
        this.routeOptimizationService = routeOptimizationService;
    }

    @PostMapping("/calculate")
    public RouteResponse calculateRoute(@RequestBody RouteRequest request) {
        return routeOptimizationService.calculateRoute(request);
    }
}