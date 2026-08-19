package com.optiroute.backend.service.cost;

import org.springframework.stereotype.Service;

import com.optiroute.backend.dto.response.route.RouteCostDetailsDto;

@Service
public class RouteCostService {

    public RouteCostDetailsDto calculateCosts(double distanceKm, double fuelConsumptionPer100Km, double fuelPrice, double tollCost) {

        double fuelUsed = (distanceKm / 100.0) * fuelConsumptionPer100Km;
        double fuelCost = fuelUsed * fuelPrice;

        RouteCostDetailsDto dto = new RouteCostDetailsDto();
        dto.setFuelCost(fuelCost);
        dto.setTollCost(tollCost);
        dto.setTotalCost(fuelCost + tollCost);

        return dto;
    }
}