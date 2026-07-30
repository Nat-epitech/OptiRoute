package com.optiroute.backend.controller.route;

import com.optiroute.backend.dto.request.route.RouteRequest;
import com.optiroute.backend.dto.response.route.RouteResponse;
import com.optiroute.backend.service.route.RouteOptimizationService;

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