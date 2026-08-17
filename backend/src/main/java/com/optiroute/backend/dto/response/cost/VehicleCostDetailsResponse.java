package com.optiroute.backend.dto.response.cost;

public record VehicleCostDetailsResponse(double depreciationCost, double insuranceCost, double maintenanceCost, double totalCost) {
}
