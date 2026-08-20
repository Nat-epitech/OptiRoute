package com.optiroute.backend.dto.request.cost;

import com.optiroute.backend.type.cost.CostConditionFieldType;
import com.optiroute.backend.type.cost.CostConditionOperatorType;
import com.optiroute.backend.type.cost.CostConditionSourceType;

public record CostConditionRequest(CostConditionSourceType source, CostConditionFieldType field, CostConditionOperatorType operator, String value) {
}
