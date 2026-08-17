package com.optiroute.backend.dto.response.cost;

import java.util.List;

public record CostCategoryResponse(List<AppliedCostResponse> costs, double totalCost) {
}
