package com.optiroute.backend.dto.response.cost;

public record TransportCostDetailsResponse(CostCategoryResponse vehicle, CostCategoryResponse driver, CostCategoryResponse structure, double totalCost) {
}