package com.optiroute.backend.service.cost;

import com.optiroute.backend.dto.response.cost.AppliedCostResponse;
import com.optiroute.backend.dto.response.cost.CostCategoryResponse;
import com.optiroute.backend.entity.cost.CostParameter;
import com.optiroute.backend.type.CostParameterCategoryType;
import com.optiroute.backend.type.CostParameterUnitType;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class StructureCostService {

    private static final String STRUCTURE_COST_LABEL = "Coût structure";

    private final CostParameterService costParameterService;
    private final WorkingDaysService workingDaysService;

    public StructureCostService(CostParameterService costParameterService, WorkingDaysService workingDaysService) {
        this.costParameterService = costParameterService;
        this.workingDaysService = workingDaysService;
    }

    public CostCategoryResponse calculateCosts(LocalDate date, int dailyTransportCount) {

        if (dailyTransportCount <= 0) {
            return new CostCategoryResponse(List.of(), 0);
        }

        CostParameter parameter = costParameterService.getParameter(CostParameterCategoryType.STRUCTURE,STRUCTURE_COST_LABEL);
        if (parameter.getUnit() != CostParameterUnitType.EUR_PER_YEAR) {
            throw new IllegalStateException("Structure cost parameter must use EUR_PER_YEAR");
        }

        int workingDaysYear = workingDaysService.getWorkingDaysInYear(date.getYear());
        if (workingDaysYear <= 0) {
            return new CostCategoryResponse(List.of(), 0);
        }

        double dailyStructureCost = parameter.getValue().doubleValue() / workingDaysYear;
        double transportStructureCost = dailyStructureCost / dailyTransportCount;

        AppliedCostResponse cost = new AppliedCostResponse(parameter.getLabel(), transportStructureCost);

        return new CostCategoryResponse(List.of(cost), transportStructureCost);
    }
}