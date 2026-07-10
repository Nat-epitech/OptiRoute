package com.optiroute.backend.service;

import com.optiroute.backend.repository.MissionRouteEstimateRepository;
import com.optiroute.backend.entity.MissionRouteEstimate;
import com.optiroute.backend.entity.Mission;
import com.optiroute.backend.dto.response.RouteResponse;
import com.optiroute.backend.client.dto.RoutesDto;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MissionRouteEstimateService {

	private final MissionRouteEstimateRepository repository;

	public MissionRouteEstimateService(MissionRouteEstimateRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public MissionRouteEstimate saveEstimate(Mission mission, RouteResponse response) {
		// TODO: Choisir route adapter selon critère (coût, distance, durée, etc.)
		RoutesDto bestRoute = response.getRoutes().getFirst();

		MissionRouteEstimate estimate = new MissionRouteEstimate();
		estimate.setMissionId(mission.getId());
		estimate.setRoutingProvider("HERE");
		estimate.setRoutingMode("FASTEST");
		estimate.setDepartureTime(mission.getPlannedStart());
		estimate.setDistanceMeters(bestRoute.getDistanceMeters());
		estimate.setDurationSeconds(bestRoute.getDurationSeconds());

		// TODO : lorsque HERE exposera la durée avec trafic
		estimate.setTrafficDurationSeconds(null);
		estimate.setPolyline(bestRoute.getPolyline());

		estimate.setEstimatedFuelCost(BigDecimal.valueOf(bestRoute.getCosts().getFuelCost()));
		estimate.setEstimatedTollCost(BigDecimal.valueOf(bestRoute.getCosts().getTollCost()));
		estimate.setEstimatedDriverCost(BigDecimal.valueOf(bestRoute.getCosts().getDriverCost()));
		estimate.setEstimatedTotalCost(BigDecimal.valueOf(bestRoute.getCosts().getTotalCost()));

		return repository.save(estimate);
	}

}