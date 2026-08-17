package com.optiroute.backend.dto.response.cost;

import java.util.List;

public record VehicleCostDetailsResponse(List<AppliedCostResponse> costs, double totalCost) {
}
