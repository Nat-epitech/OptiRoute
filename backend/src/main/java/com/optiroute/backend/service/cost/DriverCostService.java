package com.optiroute.backend.service.cost;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.optiroute.backend.dto.request.cost.CostCalculationContext;
import com.optiroute.backend.dto.response.cost.AppliedCostResponse;
import com.optiroute.backend.dto.response.cost.CostCategoryResponse;
import com.optiroute.backend.entity.driver.Driver;
import com.optiroute.backend.type.cost.CostParameterCategoryType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverCostService {
    private static final String SALARY_LABEL = "Salaire";

    private final WorkingDaysService workingDaysService;
    private final CostParameterEngine costParameterEngine;

    public CostCategoryResponse calculateCosts(Driver driver, CostCalculationContext context) {
        return calculateCosts(driver,context,List.of(context));
    }

    public CostCategoryResponse calculateCosts(Driver driver, CostCalculationContext context, List<CostCalculationContext> dailyContexts) {

        List<AppliedCostResponse> costs = new ArrayList<>();

        // Salaire conducteur
        double annualSalary = driver.getAnnualSalary().doubleValue();
        int workingDays = workingDaysService.getWorkingDaysInYear(context.date().getYear());

        double dailySalary = annualSalary / workingDays;
        double salaryCost = dailySalary * (context.durationHours() / context.dailyDriverDurationHours());

        costs.add(new AppliedCostResponse(SALARY_LABEL, salaryCost));

        // CostParameters DRIVER
        costs.addAll(costParameterEngine.calculateCosts(CostParameterCategoryType.DRIVER,context,dailyContexts));

        double totalCost = costs.stream().mapToDouble(AppliedCostResponse::amount).sum();

        return new CostCategoryResponse(costs, totalCost);
    }
}