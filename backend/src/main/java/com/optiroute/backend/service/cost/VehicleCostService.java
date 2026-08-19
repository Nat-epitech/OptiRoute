package com.optiroute.backend.service.cost;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.optiroute.backend.dto.response.cost.AppliedCostResponse;
import com.optiroute.backend.dto.response.cost.CostCategoryResponse;
import com.optiroute.backend.entity.vehicle.SemiTrailer;
import com.optiroute.backend.entity.vehicle.Tractor;
import com.optiroute.backend.type.CostParameterCategoryType;

@Service
public class VehicleCostService {
	private static final String DEPRECIATION_LABEL = "Amortissement";

	private final CostParameterEngine costParameterEngine;
	private final WorkingDaysService workingDaysService;

	public VehicleCostService(CostParameterEngine costParameterEngine, WorkingDaysService workingDaysService) {
		this.costParameterEngine = costParameterEngine;
		this.workingDaysService = workingDaysService;
	}

	public CostCategoryResponse calculateCosts(Tractor tractor, SemiTrailer semiTrailer, double distanceKm, double dailyVehicleDistanceKm, double durationHours,
		int dailyTransportCount, LocalDate date) {

		List<AppliedCostResponse> costs = new ArrayList<>();

		// Amortissement spécifique au véhicule
		double depreciationCost = calculateDepreciation(tractor,semiTrailer,distanceKm,dailyVehicleDistanceKm,date);
		costs.add(new AppliedCostResponse(DEPRECIATION_LABEL, depreciationCost));

		// Paramètres génériques véhicule
		costs.addAll(costParameterEngine.calculateCosts(CostParameterCategoryType.VEHICLE,date,distanceKm,dailyVehicleDistanceKm,durationHours,dailyTransportCount));

		double totalCost = costs.stream().mapToDouble(AppliedCostResponse::amount).sum();

		return new CostCategoryResponse(costs, totalCost);
	}

	private double calculateDepreciation(Tractor tractor, SemiTrailer semiTrailer, double distanceKm, double dailyVehicleDistanceKm, LocalDate date) {
		if (dailyVehicleDistanceKm <= 0) {
			return 0;
		}

		int workingDaysYear = workingDaysService.getWorkingDaysInYear(date.getYear());
		if (workingDaysYear <= 0) {
			return 0;
		}

		double depreciationCost = 0;
		if (isInDepreciationPeriod(tractor.getDepreciationStartDate(),tractor.getDepreciationEndDate(),date)) {
			depreciationCost += calculateVehicleDepreciation(tractor.getPurchaseCost(),workingDaysYear,distanceKm,dailyVehicleDistanceKm);
		}

		if (isInDepreciationPeriod(semiTrailer.getDepreciationStartDate(),semiTrailer.getDepreciationEndDate(),date)) {
			depreciationCost += calculateVehicleDepreciation(semiTrailer.getPurchaseCost(),workingDaysYear,distanceKm,dailyVehicleDistanceKm);
		}

		return depreciationCost;
	}

	private double calculateVehicleDepreciation(BigDecimal purchaseCost, int workingDaysYear, double distanceKm, double dailyVehicleDistanceKm) {
		if (purchaseCost == null) {
			return 0;
		}

		double dailyDepreciation = purchaseCost.doubleValue() / workingDaysYear;
		return dailyDepreciation * (distanceKm / dailyVehicleDistanceKm);
	}

	private boolean isInDepreciationPeriod(LocalDate startDate, LocalDate endDate, LocalDate date) {
		if (startDate == null || endDate == null) {
			return false;
		}

		return !date.isBefore(startDate) && !date.isAfter(endDate);
	}
}