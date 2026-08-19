package com.optiroute.backend.service.cost;

import org.springframework.stereotype.Service;

import com.optiroute.backend.entity.cost.CostParameter;
import com.optiroute.backend.repository.cost.CostParameterRepository;
import com.optiroute.backend.type.cost.CostParameterCategoryType;

import com.optiroute.backend.dto.response.cost.AppliedCostResponse;
import com.optiroute.backend.dto.request.cost.CostCalculationContext;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class CostParameterEngine {

    private final CostParameterRepository costParameterRepository;
    private final CostRuleService costRuleService;
    private final WorkingDaysService workingDaysService;

    public CostParameterEngine(CostParameterRepository costParameterRepository, CostRuleService costRuleService, WorkingDaysService workingDaysService) {
        this.costParameterRepository = costParameterRepository;
        this.costRuleService = costRuleService;
        this.workingDaysService = workingDaysService;
    }

    public List<AppliedCostResponse> calculateCosts(CostParameterCategoryType category, CostCalculationContext context) {

        List<AppliedCostResponse> costs = new ArrayList<>();

        for (CostParameter parameter : costParameterRepository.findByCategory(category)) {
            if (!costRuleService.isApplicable(parameter,context)) {
                continue;
            }

            double amount = calculateAmount(parameter,context);
            costs.add(new AppliedCostResponse(parameter.getLabel(), amount));
        }

        return costs;
    }

    private double calculateAmount(CostParameter parameter, CostCalculationContext context) {
        return switch (parameter.getUnit()) {

            case EUR_PER_KM -> context.distanceKm() * parameter.getValue().doubleValue();

            case EUR_PER_TRIP -> parameter.getValue().doubleValue();

            case EUR_PER_HOUR -> context.durationHours() * parameter.getValue().doubleValue();

            case EUR_PER_DAY -> parameter.getValue().doubleValue();

            case EUR_PER_MONTH -> calculateMonthlyCost(parameter,context);

            case EUR_PER_YEAR -> calculateYearlyCost(parameter,context);
        };
    }

    private double calculateMonthlyCost(CostParameter parameter, CostCalculationContext context) {
        int workingDays = workingDaysService.getWorkingDaysInMonth(YearMonth.from(context.date()));

        if (workingDays <= 0 || context.dailyVehicleDistanceKm() <= 0) {
            return 0;
        }

        double dailyCost = parameter.getValue().doubleValue() / workingDays;
        return dailyCost * (context.distanceKm() / context.dailyVehicleDistanceKm());
    }

    private double calculateYearlyCost(CostParameter parameter, CostCalculationContext context) {
        int workingDays = workingDaysService.getWorkingDaysInYear(context.date().getYear());

        if (workingDays <= 0 || context.dailyTransportCount() <= 0) {
            return 0;
        }

        double dailyCost = parameter.getValue().doubleValue() / workingDays;
        return dailyCost / context.dailyTransportCount();
    }
}
