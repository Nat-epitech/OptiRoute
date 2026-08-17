package com.optiroute.backend.dto.response.cost;

public record TransportCostDetailsResponse(double fuelCost, double tollCost, VehicleCostDetailsResponse vehicleCost, double totalCost) {
}