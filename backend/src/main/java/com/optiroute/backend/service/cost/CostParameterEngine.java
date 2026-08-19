package com.optiroute.backend.service.cost;

import org.springframework.stereotype.Service;

import com.optiroute.backend.entity.cost.CostParameter;
import com.optiroute.backend.entity.cost.CostRule;

import com.optiroute.backend.repository.cost.CostParameterRepository;
import com.optiroute.backend.repository.cost.CostRuleRepository;

import com.optiroute.backend.type.CostParameterCategoryType;

import com.optiroute.backend.dto.response.cost.AppliedCostResponse;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class CostParameterEngine {

    private final CostParameterRepository costParameterRepository;
    private final CostRuleRepository costRuleRepository;
    private final WorkingDaysService workingDaysService;

    public CostParameterEngine(CostParameterRepository costParameterRepository, CostRuleRepository costRuleRepository, WorkingDaysService workingDaysService) {
        this.costParameterRepository = costParameterRepository;
        this.costRuleRepository = costRuleRepository;
        this.workingDaysService = workingDaysService;
    }

    public List<AppliedCostResponse> calculateCosts(CostParameterCategoryType category, LocalDate date, double distanceKm, double dailyVehicleDistanceKm, double durationHours,
        int dailyTransportCount) {

        List<AppliedCostResponse> costs = new ArrayList<>();

        for (CostParameter parameter : costParameterRepository.findByCategory(category)) {
            List<CostRule> rules = costRuleRepository.findByCostParameterIdAndActiveTrue(parameter.getId());

            if (rules.isEmpty()) {
                continue;
            }

            // V1 : aucune condition à évaluer.
            // Une règle active sans condition = toujours applicable.

            double amount = calculateAmount(parameter,date,distanceKm,dailyVehicleDistanceKm,durationHours,dailyTransportCount);
            costs.add(new AppliedCostResponse(parameter.getLabel(), amount));
        }

        return costs;
    }

    private double calculateAmount(CostParameter parameter, LocalDate date, double distanceKm, double dailyVehicleDistanceKm, double durationHours, int dailyTransportCount) {
        return switch (parameter.getUnit()) {
            case EUR_PER_KM -> distanceKm * parameter.getValue().doubleValue();

            case EUR_PER_TRIP -> parameter.getValue().doubleValue();

            case EUR_PER_HOUR -> durationHours * parameter.getValue().doubleValue();

            case EUR_PER_DAY -> parameter.getValue().doubleValue();

            case EUR_PER_MONTH -> calculateMonthlyCost(parameter,date,distanceKm,dailyVehicleDistanceKm);

            case EUR_PER_YEAR -> calculateYearlyCost(parameter,date,dailyTransportCount);
        };
    }

    private double calculateMonthlyCost(CostParameter parameter, LocalDate date, double distanceKm, double dailyVehicleDistanceKm) {
        int workingDays = workingDaysService.getWorkingDaysInMonth(YearMonth.from(date));

        if (workingDays <= 0 || dailyVehicleDistanceKm <= 0) {
            return 0;
        }

        double dailyCost = parameter.getValue().doubleValue() / workingDays;
        return dailyCost * (distanceKm / dailyVehicleDistanceKm);
    }

    private double calculateYearlyCost(CostParameter parameter, LocalDate date, int dailyTransportCount) {

        int workingDays = workingDaysService.getWorkingDaysInYear(date.getYear());

        if (workingDays <= 0 || dailyTransportCount <= 0) {
            return 0;
        }

        double dailyCost = parameter.getValue().doubleValue() / workingDays;
        return dailyCost / dailyTransportCount;
    }
}
