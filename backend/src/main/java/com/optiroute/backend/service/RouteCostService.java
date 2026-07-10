package com.optiroute.backend.service;

import org.springframework.stereotype.Service;

import com.optiroute.backend.dto.dto.RouteCostDetailsDto;

@Service
public class RouteCostService {

    public RouteCostDetailsDto calculateCosts(double distanceKm, double durationHours, double fuelConsumptionPer100Km,
            double fuelPrice, double tollCost, double driverHourlyRate) {

        double fuelUsed = (distanceKm / 100.0) * fuelConsumptionPer100Km;
        double fuelCost = fuelUsed * fuelPrice;
        double driverCost = durationHours * driverHourlyRate;

        double total = fuelCost + tollCost + driverCost;

        RouteCostDetailsDto dto = new RouteCostDetailsDto();
        dto.setFuelCost(fuelCost);
        dto.setTollCost(tollCost);
        dto.setDriverCost(driverCost);
        dto.setTotalCost(total);

        return dto;
    }
}