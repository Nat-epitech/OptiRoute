package com.optiroute.backend.dto.response.cost;

import com.optiroute.backend.type.cost.CostConditionSourceType;
import com.optiroute.backend.type.cost.CostConditionFieldType;
import com.optiroute.backend.type.cost.CostConditionOperatorType;

public record CostConditionResponse(Long id, CostConditionSourceType source, CostConditionFieldType field, CostConditionOperatorType operator, String value) {
}