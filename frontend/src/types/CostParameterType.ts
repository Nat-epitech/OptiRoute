export type CostParameterCategory =
    | "VEHICLE"
    | "DRIVER"
    | "STRUCTURE"

export type CostParameterUnit =
    | "EUR_PER_KM"
    | "EUR_PER_TRIP"
    | "EUR_PER_HOUR"
    | "EUR_PER_DAY"
    | "EUR_PER_MONTH"
    | "EUR_PER_YEAR"

export type CostConditionSource =
    | "TRIP"
    | "VEHICLE"
    | "DRIVER"

export type CostConditionField =
    | "DISTANCE"
    | "DURATION"
    | "DEPARTURE_TIME"
    | "VEHICLE_TYPE"

export type CostConditionOperator =
    | "EQUALS"
    | "NOT_EQUALS"
    | "LESS_THAN"
    | "GREATER_THAN"
    | "BEFORE"
    | "AFTER"

export type CostRuleLogicalOperator =
    | "AND"
    | "OR"