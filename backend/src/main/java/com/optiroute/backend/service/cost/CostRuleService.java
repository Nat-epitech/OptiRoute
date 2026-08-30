package com.optiroute.backend.service.cost;

import org.springframework.stereotype.Service;
import com.optiroute.backend.entity.cost.CostRule;
import com.optiroute.backend.entity.cost.CostCondition;
import com.optiroute.backend.entity.cost.CostParameter;

import com.optiroute.backend.repository.cost.CostRuleRepository;
import com.optiroute.backend.repository.cost.CostConditionRepository;

import com.optiroute.backend.type.cost.CostRuleLogicalOperatorType;
import com.optiroute.backend.dto.request.cost.CostCalculationContext;

import java.util.List;
import java.time.LocalTime;

@Service
public class CostRuleService {

    private final CostRuleRepository costRuleRepository;
    private final CostConditionRepository costConditionRepository;

    public CostRuleService(CostRuleRepository costRuleRepository, CostConditionRepository costConditionRepository) {
        this.costRuleRepository = costRuleRepository;
        this.costConditionRepository = costConditionRepository;
    }

    public boolean isApplicable(CostParameter parameter, CostCalculationContext context) {
        List<CostRule> rules = costRuleRepository.findByCostParameterId(parameter.getId());

        // Aucune règle = toujours applicable
        if (rules.isEmpty()) {
            return true;
        }

        // Une seule règle vraie suffit
        return rules.stream().anyMatch(rule -> evaluateRule(rule,context));
    }

    private boolean evaluateRule(CostRule rule, CostCalculationContext context) {

        List<CostCondition> conditions = costConditionRepository.findByCostRuleId(rule.getId());
        if (conditions.isEmpty()) {
            return false;
        }

        if (rule.getLogicalOperator() == CostRuleLogicalOperatorType.AND) {
            return conditions.stream().allMatch(condition -> evaluateCondition(condition,context));
        }

        return conditions.stream().anyMatch(condition -> evaluateCondition(condition,context));
    }

    private boolean evaluateCondition(CostCondition condition, CostCalculationContext context) {
        return switch (condition.getField()) {

            case DISTANCE -> evaluateNumeric(context.distanceKm(),condition);

            case DURATION -> evaluateNumeric(context.durationHours(),condition);

            case DEPARTURE_TIME -> evaluateTime(context.departureTime(),condition);

            case ARRIVAL_TIME -> evaluateTime(context.arrivalTime(),condition);

            case DRIVER_DAY_START_TIME -> evaluateTime(context.driverDayStartTime(),condition);

            case DRIVER_DAY_END_TIME -> evaluateTime(context.driverDayEndTime(),condition);

            case VEHICLE_TYPE -> evaluateString(context.vehicleType(),condition);
        };
    }

    private boolean evaluateNumeric(double actualValue, CostCondition condition) {
        double expectedValue = Double.parseDouble(condition.getValue());

        return switch (condition.getOperator()) {

            case EQUALS -> actualValue == expectedValue;

            case NOT_EQUALS -> actualValue != expectedValue;

            case LESS_THAN -> actualValue < expectedValue;

            case GREATER_THAN -> actualValue > expectedValue;

            default -> throw new IllegalArgumentException("Invalid operator for numeric condition: " + condition.getOperator());
        };
    }

    private boolean evaluateTime(LocalTime actualValue, CostCondition condition) {
        LocalTime expectedValue = LocalTime.parse(condition.getValue());

        return switch (condition.getOperator()) {

            case EQUALS -> actualValue.equals(expectedValue);

            case NOT_EQUALS -> !actualValue.equals(expectedValue);

            case BEFORE -> actualValue.isBefore(expectedValue);

            case AFTER -> actualValue.isAfter(expectedValue);

            default -> throw new IllegalArgumentException("Invalid operator for time condition: " + condition.getOperator());
        };
    }

    private boolean evaluateString(String actualValue, CostCondition condition) {
        return switch (condition.getOperator()) {

            case EQUALS -> actualValue.equalsIgnoreCase(condition.getValue());

            case NOT_EQUALS -> !actualValue.equalsIgnoreCase(condition.getValue());

            default -> throw new IllegalArgumentException("Invalid operator for string condition: " + condition.getOperator());
        };
    }
}