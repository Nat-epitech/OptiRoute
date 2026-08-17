package com.optiroute.backend.service.cost;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import org.springframework.stereotype.Service;

import com.optiroute.backend.dto.response.cost.VehicleCostDetailsResponse;
import com.optiroute.backend.entity.cost.CostParameter;
import com.optiroute.backend.entity.vehicle.SemiTrailer;
import com.optiroute.backend.entity.vehicle.Tractor;
import com.optiroute.backend.type.CostParameterCategoryType;
import com.optiroute.backend.type.CostParameterUnitType;
import com.optiroute.backend.type.VehicleCostParameter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleCostService {

	private final CostParameterService costParameterService;
	private final WorkingDaysService workingDaysService;

	public VehicleCostDetailsResponse calculateCosts(Tractor tractor, SemiTrailer semiTrailer, double distanceKm, double dailyVehicleDistanceKm, LocalDate date) {

		double depreciationCost = calculateDepreciation(tractor,semiTrailer,distanceKm,dailyVehicleDistanceKm,date);
		double insuranceCost = calculateInsurance(distanceKm,dailyVehicleDistanceKm,date);
		double maintenanceCost = calculateMaintenance(distanceKm);

		double totalCost = depreciationCost + insuranceCost + maintenanceCost;

		return new VehicleCostDetailsResponse(depreciationCost, insuranceCost, maintenanceCost, totalCost);
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

	private double calculateInsurance(double distanceKm, double dailyVehicleDistanceKm, LocalDate date) {

		if (dailyVehicleDistanceKm <= 0) {
			return 0;
		}

		CostParameter parameter = costParameterService.getParameter(CostParameterCategoryType.VEHICLE,VehicleCostParameter.INSURANCE.getLabel());

		if (parameter.getUnit() != CostParameterUnitType.EUR_PER_MONTH) {
			throw new IllegalStateException("Vehicle insurance parameter must use EUR_PER_MONTH");
		}

		int workingDaysMonth = workingDaysService.getWorkingDaysInMonth(YearMonth.from(date));
		if (workingDaysMonth <= 0) {
			return 0;
		}

		double dailyInsurance = parameter.getValue().doubleValue() / workingDaysMonth;

		return dailyInsurance * (distanceKm / dailyVehicleDistanceKm);
	}

	private double calculateMaintenance(double distanceKm) {

		CostParameter parameter = costParameterService.getParameter(CostParameterCategoryType.VEHICLE,VehicleCostParameter.MAINTENANCE.getLabel());

		if (parameter.getUnit() != CostParameterUnitType.EUR_PER_KM) {
			throw new IllegalStateException("Vehicle maintenance parameter must use EUR_PER_KM");
		}

		return distanceKm * parameter.getValue().doubleValue();
	}

	private boolean isInDepreciationPeriod(LocalDate startDate, LocalDate endDate, LocalDate date) {

		if (startDate == null || endDate == null) {
			return false;
		}

		return !date.isBefore(startDate) && !date.isAfter(endDate);
	}
}